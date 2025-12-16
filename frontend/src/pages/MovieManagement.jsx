import { useState, useEffect } from 'react';
import axios from 'axios';
import { FaTrash, FaPlus, FaEdit } from 'react-icons/fa';
import './MovieManagement.css';

function MovieManagement() {
    const [movies, setMovies] = useState([]);
    const [directors, setDirectors] = useState([]);
    const [categories, setCategories] = useState([]);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingId, setEditingId] = useState(null);

    // Director inline create state
    const [isCreatingDirector, setIsCreatingDirector] = useState(false);
    const [newDirectorName, setNewDirectorName] = useState("");

    const initialFormState = {
        titulo: '',
        directorId: '',
        categoriaIds: [], // Multi-select support
        sinopsis: '',
        duracion: 120,
        fechaEstreno: new Date().toISOString().split('T')[0],
        valoracion: 5,
        urlImagen: ''
    };

    const [formData, setFormData] = useState(initialFormState);

    // Initial Data Fetch
    useEffect(() => {
        fetchMovies();
        fetchAuxData();
    }, []);

    const fetchMovies = () => {
        axios.get('http://localhost:8081/api/peliculas')
            .then(res => setMovies(res.data))
            .catch(err => console.error("Error fetching movies:", err));
    };

    const fetchAuxData = () => {
        const p1 = axios.get('http://localhost:8081/api/directores');
        const p2 = axios.get('http://localhost:8081/api/categorias');

        Promise.all([p1, p2])
            .then(([resDir, resCat]) => {
                setDirectors(resDir.data);
                setCategories(resCat.data);
            })
            .catch(err => console.error("Error fetching aux data:", err));
    };

    const handleCreateDirector = () => {
        if (!newDirectorName.trim()) return;

        axios.post('http://localhost:8081/api/directores', { nombre: newDirectorName })
            .then((res) => {
                const newDir = res.data;
                // Add to list and select it
                setDirectors([...directors, newDir]);
                setFormData({ ...formData, directorId: newDir.id });
                setIsCreatingDirector(false);
                setNewDirectorName("");
            })
            .catch(err => alert("Error creando director: " + err.message));
    };

    const handleDelete = (id) => {
        if (window.confirm('¿Seguro que quieres eliminar esta película?')) {
            axios.delete(`http://localhost:8081/api/peliculas/${id}`)
                .then(() => fetchMovies())
                .catch(err => alert("Error al eliminar: " + err.message));
        }
    };

    const handleSave = () => {
        if (!formData.titulo) return alert("El título es obligatorio");
        if (!formData.directorId) return alert("El director es obligatorio");

        const dataToSend = {
            ...formData,
            // Ensure types match DTO
            duracion: parseInt(formData.duracion),
            valoracion: parseInt(formData.valoracion),
            directorId: parseInt(formData.directorId),
            categoriaIds: Array.isArray(formData.categoriaIds)
                ? formData.categoriaIds.map(id => parseInt(id))
                : [parseInt(formData.categoriaIds)] // Fallback if simple select is used
        };

        if (editingId) {
            axios.put(`http://localhost:8081/api/peliculas/${editingId}`, dataToSend)
                .then(() => {
                    closeModal();
                    fetchMovies();
                })
                .catch(err => {
                    console.error(err);
                    alert("Error al actualizar: " + (err.response?.data?.message || err.message));
                });
        } else {
            axios.post('http://localhost:8081/api/peliculas', dataToSend)
                .then(() => {
                    closeModal();
                    fetchMovies();
                })
                .catch(err => {
                    console.error(err);
                    alert("Error al crear: " + (err.response?.data?.message || err.message));
                });
        }
    };

    const startEdit = (movie) => {
        setEditingId(movie.id);
        setFormData({
            titulo: movie.titulo,
            directorId: movie.directorId || (movie.director ? movie.director.id : ''),
            // If movie.categorias is list of objects, map to IDs. 
            // NOTE: DTO usually returns full objects, so we need to map them back to IDs for the form.
            categoriaIds: movie.categorias ? movie.categorias.map(c => c.id) : [],
            sinopsis: movie.sinopsis,
            duracion: movie.duracion,
            fechaEstreno: movie.fechaEstreno || '',
            valoracion: movie.valoracion,
            urlImagen: movie.urlImagen
        });
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setEditingId(null);
        setFormData(initialFormState);
        setIsCreatingDirector(false);
        setNewDirectorName("");
    };

    const handleCategoryChange = (e) => {
        // Handle multiple select logic
        const values = Array.from(e.target.selectedOptions, option => option.value);
        setFormData({ ...formData, categoriaIds: values });
    };

    return (
        <div className="movie-management-container">
            <h1 className="page-title">Gestión de Películas</h1>

            <button className="add-btn-primary" onClick={() => { closeModal(); setIsModalOpen(true); }}>
                <FaPlus /> Nueva Película
            </button>

            <table className="movie-table">
                <thead>
                    <tr>
                        <th>Poster</th>
                        <th>Título</th>
                        <th>Director</th>
                        <th>Puntuación</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {movies.map(movie => (
                        <tr key={movie.id}>
                            <td>
                                <img
                                    src={movie.urlImagen || "https://via.placeholder.com/50x75?text=No+Img"}
                                    alt={movie.titulo}
                                    className="table-poster"
                                    onError={(e) => { e.target.src = "https://via.placeholder.com/50x75?text=Error" }}
                                />
                            </td>
                            <td>{movie.titulo}</td>
                            <td>{movie.director ? movie.director.nombre : "N/A"}</td>
                            <td>{movie.valoracion}/10</td>
                            <td className="actions-cell">
                                <button className="icon-btn edit" onClick={() => startEdit(movie)}>
                                    <FaEdit />
                                </button>
                                <button className="icon-btn delete" onClick={() => handleDelete(movie.id)}>
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
                        <h2>{editingId ? 'Editar Película' : 'Nueva Película'}</h2>

                        <div className="form-grid">
                            <div className="form-group full-width">
                                <label>Título</label>
                                <input
                                    value={formData.titulo}
                                    onChange={e => setFormData({ ...formData, titulo: e.target.value })}
                                    placeholder="Ej: Inception"
                                />
                            </div>

                            <div className="form-group">
                                <label>Director</label>
                                <div className="director-input-group">
                                    <select
                                        value={formData.directorId}
                                        onChange={e => setFormData({ ...formData, directorId: e.target.value })}
                                        style={{ flex: 1 }}
                                    >
                                        <option value="">Seleccionar...</option>
                                        {directors.map(d => (
                                            <option key={d.id} value={d.id}>{d.nombre}</option>
                                        ))}
                                    </select>
                                    <button
                                        className="add-director-btn"
                                        onClick={() => setIsCreatingDirector(!isCreatingDirector)}
                                        title="Nuevo Director"
                                    >
                                        <FaPlus size={12} />
                                    </button>
                                </div>

                                {isCreatingDirector && (
                                    <div className="inline-create-container">
                                        <input
                                            type="text"
                                            placeholder="Nombre del Director"
                                            value={newDirectorName}
                                            onChange={(e) => setNewDirectorName(e.target.value)}
                                        />
                                        <button className="inline-btn save" onClick={handleCreateDirector}>✓</button>
                                        <button className="inline-btn cancel" onClick={() => setIsCreatingDirector(false)}>✕</button>
                                    </div>
                                )}
                            </div>

                            <div className="form-group">
                                <label>Categorías (Ctrl+Click para múltiple)</label>
                                <select
                                    multiple
                                    value={formData.categoriaIds}
                                    onChange={handleCategoryChange}
                                    style={{ height: '100px' }}
                                >
                                    {categories.map(c => (
                                        <option key={c.id} value={c.id}>{c.nombre}</option>
                                    ))}
                                </select>
                            </div>

                            <div className="form-group">
                                <label>Fecha Estreno</label>
                                <input
                                    type="date"
                                    value={formData.fechaEstreno}
                                    onChange={e => setFormData({ ...formData, fechaEstreno: e.target.value })}
                                />
                            </div>

                            <div className="form-group">
                                <label>Duración (min)</label>
                                <input
                                    type="number"
                                    value={formData.duracion}
                                    onChange={e => setFormData({ ...formData, duracion: e.target.value })}
                                />
                            </div>

                            <div className="form-group">
                                <label>Puntuación (0-10)</label>
                                <input
                                    type="number"
                                    min="0" max="10"
                                    value={formData.valoracion}
                                    onChange={e => setFormData({ ...formData, valoracion: e.target.value })}
                                />
                            </div>

                            <div className="form-group full-width">
                                <label>URL Poster</label>
                                <input
                                    value={formData.urlImagen}
                                    onChange={e => setFormData({ ...formData, urlImagen: e.target.value })}
                                    placeholder="https://..."
                                />
                            </div>

                            <div className="form-group full-width">
                                <label>Sinopsis</label>
                                <textarea
                                    value={formData.sinopsis}
                                    onChange={e => setFormData({ ...formData, sinopsis: e.target.value })}
                                />
                            </div>
                        </div>

                        <div className="modal-actions">
                            <button onClick={handleSave} className="save-btn">Guardar</button>
                            <button onClick={closeModal} className="cancel-btn">Cancelar</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default MovieManagement;
