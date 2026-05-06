const API_URL = 'http://localhost:8080/api';

let currentEditId = null;

// Cargar estudiantes al iniciar
document.addEventListener('DOMContentLoaded', () => {
    loadEstudiantes();

    document.getElementById('form').addEventListener('submit', async (e) => {
        e.preventDefault();
        await saveEstudiante();
    });
});

async function loadEstudiantes() {
    try {
        const response = await fetch(`${API_URL}/estudiantes`);
        const estudiantes = await response.json();
        displayEstudiantes(estudiantes);
    } catch (error) {
        console.error('Error cargando estudiantes:', error);
        alert('Error al cargar los estudiantes');
    }
}

function displayEstudiantes(estudiantes) {
    const tbody = document.getElementById('estudiantesList');
    tbody.innerHTML = '';

    estudiantes.forEach(estudiante => {
        const row = tbody.insertRow();
        row.insertCell(0).textContent = estudiante.id;
        row.insertCell(1).textContent = estudiante.nombre;
        row.insertCell(2).textContent = estudiante.apellido;
        row.insertCell(3).textContent = estudiante.correo;

        const actionsCell = row.insertCell(4);
        actionsCell.className = 'action-buttons';

        const editBtn = document.createElement('button');
        editBtn.textContent = 'Editar';
        editBtn.className = 'btn-edit';
        editBtn.onclick = () => editEstudiante(estudiante);

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'Eliminar';
        deleteBtn.className = 'btn-delete';
        deleteBtn.onclick = () => deleteEstudiante(estudiante.id);

        const notasBtn = document.createElement('button');
        notasBtn.textContent = 'Ver Notas';
        notasBtn.className = 'btn-notas';
        notasBtn.onclick = () => window.location.href = `nota.html?id=${estudiante.id}`;

        const calcularBtn = document.createElement('button');
        calcularBtn.textContent = 'Calcular NF';
        calcularBtn.className = 'btn-calcular';
        calcularBtn.onclick = () => calcularNotaFinal(estudiante.id);

        actionsCell.appendChild(editBtn);
        actionsCell.appendChild(deleteBtn);
        actionsCell.appendChild(notasBtn);
        actionsCell.appendChild(calcularBtn);
    });
}

function showForm() {
    currentEditId = null;
    document.getElementById('formTitle').textContent = 'Crear Estudiante';
    document.getElementById('estudianteForm').classList.add('active');
    document.getElementById('form').reset();
    document.getElementById('estudianteId').value = '';
}

function hideForm() {
    document.getElementById('estudianteForm').classList.remove('active');
}

async function saveEstudiante() {
    const id = document.getElementById('estudianteId').value;
    const estudiante = {
        nombre: document.getElementById('nombre').value,
        apellido: document.getElementById('apellido').value,
        correo: document.getElementById('correo').value
    };

    try {
        let response;
        if (id) {
            response = await fetch(`${API_URL}/estudiantes/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(estudiante)
            });
        } else {
            response = await fetch(`${API_URL}/estudiantes`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(estudiante)
            });
        }

        if (response.ok) {
            alert(id ? 'Estudiante actualizado' : 'Estudiante creado');
            hideForm();
            loadEstudiantes();
        } else {
            const error = await response.json();
            alert('Error: ' + (error.message || 'Ocurrió un error'));
        }
    } catch (error) {
        console.error('Error guardando estudiante:', error);
        alert('Error al guardar el estudiante');
    }
}

function editEstudiante(estudiante) {
    currentEditId = estudiante.id;
    document.getElementById('formTitle').textContent = 'Editar Estudiante';
    document.getElementById('estudianteId').value = estudiante.id;
    document.getElementById('nombre').value = estudiante.nombre;
    document.getElementById('apellido').value = estudiante.apellido;
    document.getElementById('correo').value = estudiante.correo;
    document.getElementById('estudianteForm').classList.add('active');
}

async function deleteEstudiante(id) {
    if (confirm('¿Estás seguro de eliminar este estudiante? Se eliminarán todas sus notas.')) {
        try {
            const response = await fetch(`${API_URL}/estudiantes/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Estudiante eliminado');
                loadEstudiantes();
            } else {
                alert('Error al eliminar el estudiante');
            }
        } catch (error) {
            console.error('Error eliminando estudiante:', error);
            alert('Error al eliminar el estudiante');
        }
    }
}

async function calcularNotaFinal(id) {
    try {
        const response = await fetch(`${API_URL}/estudiantes/${id}/nota-final`);
        if (response.ok) {
            const data = await response.json();
            document.getElementById('notaFinalResult').innerHTML = `
                <strong>Nota Final: ${data.notaFinal}</strong><br>
                <span style="color: ${data.notaFinal >= 3 ? 'green' : 'red'}">
                    ${data.notaFinal >= 3 ? 'APROBADO' : 'REPROBADO'}
                </span>
            `;
            document.getElementById('notaFinalModal').style.display = 'block';
        } else {
            const error = await response.json();
            alert('Error: ' + (error.message || 'No se pudo calcular la nota final'));
        }
    } catch (error) {
        console.error('Error calculando nota final:', error);
        alert('Error al calcular la nota final');
    }
}

function closeNotaFinalModal() {
    document.getElementById('notaFinalModal').style.display = 'none';
}