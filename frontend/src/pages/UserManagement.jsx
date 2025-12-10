import { useState, useEffect } from 'react';
import axios from 'axios';
import { FaTrash, FaUserPlus, FaEdit } from 'react-icons/fa';
import './UserManagement.css';

function UserManagement() {
    const [users, setUsers] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState({ username: '', email: '', password: '1234' });
    const [editingId, setEditingId] = useState(null);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = () => {
        axios.get('http://localhost:8081/api/usuarios')
            .then(res => setUsers(res.data))
            .catch(err => console.error(err));
    };

    const handleDelete = (e, id) => {
        e.stopPropagation();
        if (window.confirm('¿Seguro que quieres eliminar este usuario?')) {
            axios.delete(`http://localhost:8081/api/usuarios/${id}`)
                .then(() => fetchUsers())
                .catch(err => {
                    console.error(err);
                    alert("Error al borrar usuario: " + (err.response?.data?.message || err.message));
                });
        }
    };

    const handleSave = () => {
        if (!formData.username) return alert("El nombre es obligatorio");

        // Auto-generate email if empty or editing
        const dataToSend = {
            ...formData,
            email: formData.email || `${formData.username.toLowerCase().replace(/\s/g, '')}@cine.com`,
            rol: "USER"
        };

        if (editingId) {
            axios.put(`http://localhost:8081/api/usuarios/${editingId}`, dataToSend)
                .then(() => {
                    setIsModalOpen(false);
                    fetchUsers();
                    resetForm();
                })
                .catch(err => console.error(err));
        } else {
            axios.post('http://localhost:8081/api/usuarios', dataToSend)
                .then(() => {
                    setIsModalOpen(false);
                    fetchUsers();
                    resetForm();
                })
                .catch(err => {
                    console.error(err);
                    alert("Error al crear usuario. ¿Quizás ya existe?");
                });
        }
    };

    const startEdit = (user) => {
        setEditingId(user.id);
        setFormData({ username: user.username, email: user.email, password: '' });
        setIsModalOpen(true);
    };

    const resetForm = () => {
        setFormData({ username: '', email: '', password: '1234' });
        setEditingId(null);
    };

    return (
        <div className="user-management-container">
            <h1 className="page-title">Gestión de Usuarios</h1>

            <button className="add-btn-primary" onClick={() => { resetForm(); setIsModalOpen(true); }}>
                <FaUserPlus /> Nuevo Usuario
            </button>

            <table className="user-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Avatar</th>
                        <th>Usuario</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>
                                <img
                                    src={`https://api.dicebear.com/7.x/avataaars/svg?seed=${user.username}`}
                                    className="table-avatar"
                                    alt="avatar"
                                />
                            </td>
                            <td>{user.username}</td>
                            <td>{user.email}</td>
                            <td>{user.rol || 'USER'}</td>
                            <td className="actions-cell">
                                <button className="icon-btn edit" onClick={() => startEdit(user)}>
                                    <FaEdit />
                                </button>
                                <button className="icon-btn delete" onClick={(e) => handleDelete(e, user.id)}>
                                    <FaTrash />
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>{editingId ? 'Editar Usuario' : 'Nuevo Usuario'}</h2>

                        <div className="form-group">
                            <label>Nombre de Usuario</label>
                            <input
                                type="text"
                                value={formData.username}
                                onChange={e => setFormData({ ...formData, username: e.target.value })}
                            />
                        </div>

                        <div className="form-group">
                            <label>Email (Opcional)</label>
                            <input
                                type="email"
                                value={formData.email}
                                onChange={e => setFormData({ ...formData, email: e.target.value })}
                            />
                        </div>

                        <div className="form-group">
                            <label>Contraseña (Opcional)</label>
                            <input
                                type="password"
                                placeholder="Dejar en blanco para no cambiar"
                                value={formData.password}
                                onChange={e => setFormData({ ...formData, password: e.target.value })}
                            />
                        </div>

                        <div className="modal-actions">
                            <button onClick={handleSave} className="save-btn">Guardar</button>
                            <button onClick={() => setIsModalOpen(false)} className="cancel-btn">Cancelar</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default UserManagement;
