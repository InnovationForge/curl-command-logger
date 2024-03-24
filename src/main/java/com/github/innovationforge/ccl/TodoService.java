package com.github.innovationforge.ccl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService {

    private final List<Todo> todos = new ArrayList<>();
    private long nextId = 1;

    // Initialize with some sample todos
    {
        todos.add(new Todo(nextId++, "Buy groceries", "Milk, eggs, bread", false));
        todos.add(new Todo(nextId++, "Finish project", "Complete the Spring Boot app", false));
        todos.add(new Todo(nextId++, "Exercise", "Go for a run", false));
    }


    public List<Todo> getAllTodos() {
        return todos;
    }

    public Todo getTodoById(Long id) {
        return todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Todo createTodo(Todo todo) {
        todo.setId(nextId++);
        todos.add(todo);
        return todo;
    }

    public Todo updateTodo(Long id, Todo updatedTodo) {
        Todo existingTodo = getTodoById(id);
        if (existingTodo != null) {
            existingTodo.setTitle(updatedTodo.getTitle());
            existingTodo.setDescription(updatedTodo.getDescription());
            existingTodo.setCompleted(updatedTodo.isCompleted());
            return existingTodo;
        }
        return null;
    }

    public boolean deleteTodo(Long id) {
        return todos.removeIf(todo -> todo.getId().equals(id));
    }
}
