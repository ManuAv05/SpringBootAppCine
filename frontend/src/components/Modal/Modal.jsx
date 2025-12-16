import { useState, useEffect } from 'react';
import axios from 'axios';
import { FaTimes, FaPlay, FaPlus, FaCheck, FaThumbsUp, FaStar } from 'react-icons/fa';
import './Modal.css';
import { useAppContext } from '../../context/AppContext';

function Modal({ movie, onClose, onPlay }) {
    const { isEnMiLista, toggleMiLista, currentProfile } = useAppContext();
    const [reviews, setReviews] = useState([]);
    const [newComment, setNewComment] = useState("");
    const [newRating, setNewRating] = useState(5);
    const [hoverRating, setHoverRating] = useState(null);
    const [loadingReviews, setLoadingReviews] = useState(true);

    if (!movie) return null;

    const estaEnMiLista = isEnMiLista(movie.id);

    const getBackdrop = (peli) => {
        if (!peli) return "https://via.placeholder.com/1920x1080?text=No+Image";
        if (peli.urlImagen && peli.urlImagen.startsWith("http")) return peli.urlImagen;
        return `https://picsum.photos/seed/${peli.id}/1920/1080`;
    };

    useEffect(() => {
        if (movie?.id) {
            setLoadingReviews(true);
            axios.get(`http://localhost:8081/api/criticas/pelicula/${movie.id}`)
                .then(res => {
                    setReviews(res.data);
                    setLoadingReviews(false);
                })
                .catch(err => {
                    console.error("Error fetching reviews", err);
                    setLoadingReviews(false);
                });
        }
    }, [movie]);

    const handleSubmitReview = (e) => {
        e.preventDefault();
        if (!currentProfile) return alert("Debes seleccionar un perfil para comentar.");
        if (!newComment.trim()) return;

        const reviewData = {
            comentario: newComment,
            nota: newRating,
            usuarioId: currentProfile.id,
            peliculaId: movie.id
        };

        axios.post('http://localhost:8081/api/criticas', reviewData)
            .then(res => {
                setReviews([...reviews, res.data]);
                setNewComment("");
                setNewRating(5);
            })
            .catch(err => {
                console.error("Error posting review", err);
                alert("Error al enviar el comentario");
            });
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button className="modal-close-btn" onClick={onClose}>
                    <FaTimes size={20} />
                </button>

                <div
                    className="modal-hero"
                    style={{ backgroundImage: `url("${getBackdrop(movie)}")` }}
                >
                    <div className="modal-hero-fade" />
                    <div className="modal-hero-content">
                        <h1 className="modal-title-text">{movie.titulo}</h1>
                        <div className="modal-btns">
                            <button className="btn-play" onClick={() => onPlay(movie)}>
                                <FaPlay size={15} /> Reproducir
                            </button>
                            <button
                                className="btn-circle"
                                onClick={() => toggleMiLista(movie)}
                                title={estaEnMiLista ? "Quitar de mi lista" : "Añadir a mi lista"}
                                style={{ borderColor: estaEnMiLista ? '#46d369' : 'rgba(255,255,255,0.4)' }}
                            >
                                {estaEnMiLista ? <FaCheck /> : <FaPlus />}
                            </button>
                            <button className="btn-circle"><FaThumbsUp /></button>
                        </div>
                    </div>
                </div>

                <div className="modal-body">
                    <div className="modal-col-left">
                        <div className="modal-meta-data">
                            <span className="match-score">98% para ti</span>
                            <span>{(movie.fechaEstreno || "2024").split('-')[0]}</span>
                            <span className="age-badge">16+</span>
                            <span>{movie.duracion ? `${movie.duracion} min` : "1h 30m"}</span>
                            <span style={{ border: '1px solid white', fontSize: '0.7rem', padding: '0 3px' }}>HD</span>
                        </div>
                        <p className="modal-description">
                            {movie.sinopsis || "No hay descripción disponible."}
                        </p>
                    </div>
                    <div className="modal-col-right">
                        <div className="modal-details-item">
                            <span className="label-gray">Director: </span>
                            <span className="link-white">
                                {movie.nombreDirector || "Desconocido"}
                            </span>
                        </div>
                        <div className="modal-details-item">
                            <span className="label-gray">Reparto: </span>
                            <span className="link-white">
                                {movie.nombresActores && movie.nombresActores.length > 0
                                    ? movie.nombresActores.slice(0, 5).join(", ")
                                    : "Actores desconocidos"}
                            </span>
                        </div>
                        <div className="modal-details-item">
                            <span className="label-gray">Géneros: </span>
                            <span className="link-white">
                                {movie.nombresCategorias && movie.nombresCategorias.length > 0
                                    ? movie.nombresCategorias.join(", ")
                                    : "General"}
                            </span>
                        </div>
                    </div>
                </div>

                <div className="modal-reviews-section">
                    <h3>Comentarios y Críticas</h3>

                    <div className="reviews-list">
                        {loadingReviews ? (
                            <p>Cargando opiniones...</p>
                        ) : reviews.length > 0 ? (
                            reviews.map(review => (
                                <div key={review.id} className="review-item">
                                    <div className="review-header">
                                        <div className="review-user">
                                            <img
                                                src={`https://api.dicebear.com/7.x/avataaars/svg?seed=${review.usuario}`}
                                                alt="avatar"
                                                className="review-avatar"
                                            />
                                            <span className="review-username">{review.usuario}</span>
                                        </div>
                                        <div className="review-meta">
                                            <span className="review-rating"><FaStar color="#e50914" /> {review.nota}/10</span>
                                            <span className="review-date">{review.fecha}</span>
                                        </div>
                                    </div>
                                    <p className="review-text">{review.comentario}</p>
                                </div>
                            ))
                        ) : (
                            <p className="no-reviews">Aún no hay críticas. ¡Sé el primero!</p>
                        )}
                    </div>

                    {currentProfile && (
                        <form className="review-form" onSubmit={handleSubmitReview}>
                            <h4>Escribe tu opinión</h4>
                            <div className="form-row">
                                <div className="rating-input-stars">
                                    <label>Tu nota:</label>
                                    <div className="stars-container">
                                        {[...Array(5)].map((star, index) => {
                                            const ratingValue = index + 1;
                                            return (
                                                <label key={index}>
                                                    <input
                                                        type="radio"
                                                        name="rating"
                                                        value={ratingValue}
                                                        onClick={() => setNewRating(ratingValue)}
                                                        style={{ display: 'none' }}
                                                    />
                                                    <FaStar
                                                        className="star"
                                                        color={ratingValue <= (hoverRating || newRating) ? "#e50914" : "#444"}
                                                        size={25}
                                                        onMouseEnter={() => setHoverRating(ratingValue)}
                                                        onMouseLeave={() => setHoverRating(null)}
                                                        style={{ cursor: 'pointer', transition: 'color 0.2s', marginRight: '5px' }}
                                                    />
                                                </label>
                                            );
                                        })}
                                    </div>
                                    <span className="rating-value-text" style={{ color: '#aaa', fontSize: '0.8rem', marginLeft: '10px' }}>{newRating}/5</span>
                                </div>
                                <textarea
                                    placeholder={`Opina sobre ${movie.titulo}...`}
                                    value={newComment}
                                    onChange={e => setNewComment(e.target.value)}
                                    required
                                />
                            </div>
                            <button type="submit" className="btn-submit-review">Publicar</button>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Modal;
