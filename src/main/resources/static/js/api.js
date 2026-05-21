// Configuração da API
const API_BASE_URL = 'http://localhost:8080';

// Estado global
let authToken = localStorage.getItem('token');
let userEmail = localStorage.getItem('userEmail');
let userNivel = null;

// Função para fazer requisições
async function apiRequest(endpoint, method = 'GET', body = null, requiresToken = true) {
    const headers = {
        'Content-Type': 'application/json',
    };

    if (requiresToken && authToken) {
        headers['Authorization'] = authToken;
    }

    const config = {
        method,
        headers,
    };

    if (body && !(body instanceof FormData)) {
        config.body = JSON.stringify(body);
    } else if (body instanceof FormData) {
        delete headers['Content-Type'];
        config.body = body;
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);

        if (response.status === 401) {
            localStorage.removeItem('token');
            localStorage.removeItem('userEmail');
            window.location.href = 'index.html';
            throw new Error('Sessão expirada. Faça login novamente.');
        }

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro na requisição');
        }

        if (response.status === 204) {
            return null;
        }

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        }

        return await response.text();
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}