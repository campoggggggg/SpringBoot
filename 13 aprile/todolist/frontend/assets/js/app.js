// App Logic
document.addEventListener('DOMContentLoaded', () => {
    // State management
    let todos = [];
    let showingExpired = false;

    // DOM Elements
    const todoGrid = document.getElementById('todo-grid');
    const emptyState = document.getElementById('empty-state');
    const toggleExpiredBtn = document.getElementById('toggle-expired');
    const statTotal = document.getElementById('stat-total');
    const statPending = document.getElementById('stat-pending');
    const statDone = document.getElementById('stat-done');
    const taskModal = document.getElementById('task-modal');
    const todoForm = document.getElementById('todo-form');
    const listTitle = document.getElementById('list-title');

    // Init Lucide icons
    const initIcons = () => lucide.createIcons();

    // Fetch and Render
    const loadTodos = async () => {
        try {
            if (showingExpired) {
                todos = await window.TodoService.getScaduti();
            } else {
                todos = await window.TodoService.getAll();
            }
            renderTodos();
            updateStats();
        } catch (error) {
            console.error('Errore nel caricamento:', error);
            alert('Errore di connessione al backend! Verifica che Spring Boot sia attivo.');
        }
    };

    const updateStats = () => {
        statTotal.innerText = todos.length;
        statPending.innerText = todos.filter(t => t.statoTask === 'IN_PROGRESS' || t.statoTask === 'TODO').length;
        statDone.innerText = todos.filter(t => t.statoTask === 'DONE').length;
    };

    const renderTodos = () => {
        todoGrid.innerHTML = '';
        
        if (todos.length === 0) {
            emptyState.classList.remove('hidden');
        } else {
            emptyState.classList.add('hidden');
            todos.forEach(todo => {
                const card = createTodoCard(todo);
                todoGrid.appendChild(card);
            });
        }
        initIcons();
    };

    const createTodoCard = (todo) => {
        const div = document.createElement('div');
        div.className = 'glass-card p-6 todo-item animate-in flex flex-col justify-between';
        
        const priorityColors = {
            1: 'text-indigo-400',
            2: 'text-yellow-400',
            3: 'text-orange-500',
            4: 'text-red-500'
        };

        const priorityLabels = {
            1: 'Bassa',
            2: 'Media',
            3: 'Alta',
            4: 'Urgente'
        };

        const canTransit = (status) => {
            if (todo.statoTask === 'TODO') return status === 'IN_PROGRESS' || status === 'CANCELLED';
            if (todo.statoTask === 'IN_PROGRESS') return status === 'DONE' || status === 'CANCELLED';
            return false;
        };

        const isExpired = new Date(todo.dataScadenza) < new Date() && todo.statoTask !== 'DONE';

        div.innerHTML = `
            <div>
                <div class="flex items-center justify-between mb-4">
                    <span class="badge status-${todo.statoTask.toLowerCase()}">${todo.statoTask.replace('_', ' ')}</span>
                    <span class="text-xs font-bold ${priorityColors[todo.priorità]} flex items-center gap-1">
                        <i data-lucide="alert-circle" class="w-3 h-3"></i>
                        Priorità ${priorityLabels[todo.priorità]}
                    </span>
                </div>
                <h4 class="text-lg font-bold text-white mb-2 leading-tight">${todo.descrizione}</h4>
                <div class="flex items-center gap-2 text-sm text-gray-400 mb-6">
                    <i data-lucide="calendar" class="w-4 h-4"></i>
                    <span class="${isExpired ? 'text-red-400 font-bold' : ''}">
                        Scadenza: ${todo.dataScadenza} 
                        ${isExpired ? ' (Scaduto!)' : ''}
                    </span>
                </div>
            </div>

            <div class="flex items-center justify-between pt-4 border-t border-white/10">
                <div class="flex gap-2">
                    ${canTransit('IN_PROGRESS') ? `
                        <button onclick="handleTransition(${todo.id}, 'IN_PROGRESS')" class="p-2 bg-blue-500/20 text-blue-400 rounded-lg hover:bg-blue-500/30 transition-all" title="Metti in corso">
                            <i data-lucide="play" class="w-4 h-4"></i>
                        </button>
                    ` : ''}
                    ${canTransit('DONE') ? `
                        <button onclick="handleTransition(${todo.id}, 'DONE')" class="p-2 bg-emerald-500/20 text-emerald-400 rounded-lg hover:bg-emerald-500/30 transition-all" title="Completa">
                            <i data-lucide="check" class="w-4 h-4"></i>
                        </button>
                    ` : ''}
                    ${canTransit('CANCELLED') ? `
                        <button onclick="handleTransition(${todo.id}, 'CANCELLED')" class="p-2 bg-red-500/20 text-red-400 rounded-lg hover:bg-red-500/30 transition-all" title="Annulla">
                            <i data-lucide="x-circle" class="w-4 h-4"></i>
                        </button>
                    ` : ''}
                </div>
                <div class="flex gap-2">
                    <button onclick="editTodo(${todo.id})" class="p-2 bg-white/5 text-gray-400 rounded-lg hover:bg-white/10 transition-all">
                        <i data-lucide="edit-3" class="w-4 h-4"></i>
                    </button>
                    <button onclick="confirmDelete(${todo.id})" class="p-2 bg-red-500/10 text-red-400/60 rounded-lg hover:bg-red-500/20 hover:text-red-400 transition-all">
                        <i data-lucide="trash-2" class="w-4 h-4"></i>
                    </button>
                </div>
            </div>
        `;
        return div;
    };

    // Actions
    window.handleTransition = async (id, newStatus) => {
        const todo = todos.find(t => t.id === id);
        if (!todo) return;
        
        try {
            await window.TodoService.update(id, {
                ...todo,
                statoTask: newStatus
            });
            loadTodos();
        } catch (error) {
            console.error('Errore transizione:', error);
        }
    };

    window.confirmDelete = async (id) => {
        if (confirm('Sei sicuro di voler eliminare questo task?')) {
            try {
                await window.TodoService.delete(id);
                loadTodos();
            } catch (error) {
                console.error('Errore eliminazione:', error);
            }
        }
    };

    window.editTodo = (id) => {
        const todo = todos.find(t => t.id === id);
        if (!todo) return;
        
        document.getElementById('task-id').value = todo.id;
        document.getElementById('task-desc').value = todo.descrizione;
        document.getElementById('task-priority').value = todo.priorità;
        document.getElementById('task-status').value = todo.statoTask;
        document.getElementById('task-date').value = todo.dataScadenza;
        
        document.getElementById('modal-title').innerText = 'Modifica Task';
        taskModal.classList.remove('hidden');
    };

    // Event Listeners
    document.getElementById('add-task-btn').addEventListener('click', () => {
        todoForm.reset();
        document.getElementById('task-id').value = '';
        document.getElementById('modal-title').innerText = 'Nuovo Task';
        taskModal.classList.remove('hidden');
    });

    document.getElementById('close-modal').addEventListener('click', () => {
        taskModal.classList.add('hidden');
    });

    todoForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('task-id').value;
        const taskData = {
            descrizione: document.getElementById('task-desc').value,
            priorità: parseInt(document.getElementById('task-priority').value),
            statoTask: document.getElementById('task-status').value,
            dataScadenza: document.getElementById('task-date').value
        };

        try {
            if (id) {
                // Update
                const currentTodo = todos.find(t => t.id == id);
                // Basic client side validation matching backend logic if needed
                await window.TodoService.update(id, taskData);
            } else {
                // Create
                const result = await window.TodoService.create(taskData);
                if (!result) {
                    alert('Impossibile creare il task. Controlla che la data non sia nel passato!');
                    return;
                }
            }
            taskModal.classList.add('hidden');
            loadTodos();
        } catch (error) {
            console.error('Errore salvataggio:', error);
            alert('Errore nel salvataggio del task. Verifica che la transizione di stato sia consentita.');
        }
    });

    toggleExpiredBtn.addEventListener('click', () => {
        showingExpired = !showingExpired;
        toggleExpiredBtn.classList.toggle('bg-orange-500/20', showingExpired);
        toggleExpiredBtn.classList.toggle('text-orange-400', showingExpired);
        listTitle.innerText = showingExpired ? 'Task Scaduti' : 'I Tuoi Task';
        loadTodos();
    });

    // Start
    loadTodos();
});
