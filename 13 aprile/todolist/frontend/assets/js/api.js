// API Configuration
const API_BASE_URL = 'http://localhost:8080/todo';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

const TodoService = {
    getAll: async () => {
        const response = await api.get('');
        return response.data;
    },
    getScaduti: async () => {
        const response = await api.get('/scaduti');
        return response.data;
    },
    create: async (todo) => {
        const response = await api.post('', todo);
        return response.data;
    },
    update: async (id, todo) => {
        const response = await api.put(`/${id}`, todo);
        return response.data;
    },
    delete: async (id) => {
        const response = await api.delete(`/${id}`);
        return response.data;
    }
};

window.TodoService = TodoService;
