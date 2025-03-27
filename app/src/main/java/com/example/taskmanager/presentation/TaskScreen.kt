package com.example.taskmanager.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.data.Task
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel = hiltViewModel() ) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDesc by remember { mutableStateOf("") }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold (
        topBar = { TopAppBar(title = { Text("Task Manager") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                icon = { Icon(Icons.Default.Add, "Add") },
                text = { Text("New Task") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            DebugTools(viewModel)
            Button(
                onClick = { viewModel.syncWithRemote() },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Sync with Server")
            }

            if (showDialog || editingTask != null) {
                var editTitle by remember { mutableStateOf(editingTask?.title ?: "") }
                var editDesc by remember { mutableStateOf(editingTask?.description ?: "") }
                AddOrEditTaskDialog(
                    title = editTitle,
                    description = editDesc,
                    onTitleChange = { editTitle = it },
                    onDescChange = { editDesc = it },
                    onDismiss = {
                        showDialog = false
                        editingTask = null
                    },
                    onConfirm = {
                        if (editingTask != null) {
                            viewModel.editTask(editingTask!!, editTitle, editDesc)
                            editingTask = null
                        } else {
                            viewModel.addTask(editTitle, editDesc)
                        }
                        showDialog = false
                    }
                )
            }
            LazyColumn {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTaskCompletion(task) },
                        onDelete = { viewModel.deleteTask(task) },
                        onEdit = {
                            newTaskTitle = task.title
                            newTaskDesc = task.description
                            editingTask = task
                        }
                    )
                }
            }

            //DebugTools(viewModel)
        }
    }

    if (showDialog) {
        AddOrEditTaskDialog(
            title = newTaskTitle,
            description = newTaskDesc,
            onTitleChange = { newTaskTitle = it },
            onDescChange = { newTaskDesc = it },
            onDismiss = { showDialog = false },
            onConfirm = {
                viewModel.addTask(newTaskTitle, newTaskDesc)
                newTaskTitle = ""
                newTaskDesc = ""
                showDialog = false
            }
        )
    }
}

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.bodyMedium)
                Text(task.description, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete")
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, "Edit")
            }
        }
    }
}

@Composable
fun DebugTools(viewModel: TaskViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Debug Tools", style = MaterialTheme.typography.titleSmall)
        Button(onClick = { viewModel.triggerTestCrash() }) {
            Text("Trigger Test Crash")
        }
        Button(onClick = { viewModel.triggerDatabaseError() }) {
            Text("Trigger DB Error")
        }
    }
}

@Composable
fun AddOrEditTaskDialog(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add/Edit New Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescChange,
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Add/Edit Task")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}