// Funções de Autenticação

async function login(email, senha) {
    try {
        showLoading(true);

        const response = await fetch(`${API_BASE_URL}/apis/acesso/autenticar?login=${encodeURIComponent(email)}&senha=${senha}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Email ou senha inválidos');
        }

        const token = await response.text();

        authToken = token;
        userEmail = email;

        localStorage.setItem('token', token);
        localStorage.setItem('userEmail', email);

        showAlert(`Bem-vindo, ${email}!`, 'success');

        // Redirecionar baseado no email
        if (email === 'admin@pm.br') {
            window.location.href = 'admin.html';
        } else {
            window.location.href = 'cidadao.html';
        }

    } catch (error) {
        showAlert(error.message, 'error');
    } finally {
        showLoading(false);
    }
}

async function register(cpf, email, senha) {
    try {
        showLoading(true);

        const usuario = {
            cpf: cpf,
            email: email,
            senha: parseInt(senha),
            nivel: 2
        };

        await apiRequest('/apis/acesso/cadastrar-cidadao', 'POST', usuario, false);

        showAlert('Cadastro realizado com sucesso! Faça login.', 'success');
        switchTab('login');

    } catch (error) {
        showAlert(error.message, 'error');
    } finally {
        showLoading(false);
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
    window.location.href = 'index.html';
}