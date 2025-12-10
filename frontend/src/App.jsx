import { useEffect, useState } from 'react';
import axios from 'axios';
import { FaSearch, FaPlay, FaInfoCircle, FaTimes, FaPlus, FaThumbsUp, FaCheck } from 'react-icons/fa';
import YouTube from 'react-youtube';
import movieTrailer from 'movie-trailer';
import './index.css';

function App() {
    const [peliculas, setPeliculas] = useState([]);
    const [peliculaHero, setPeliculaHero] = useState(null);
    const [busqueda, setBusqueda] = useState("");
    const [navbarBlack, setNavbarBlack] = useState(false);

    const [selectedMovie, setSelectedMovie] = useState(null);
    const [miLista, setMiLista] = useState(() => {
        const guardado = localStorage.getItem("miListaJavaflix");
        return guardado ? JSON.parse(guardado) : [];
    });
    const [trailerUrl, setTrailerUrl] = useState("");

    // --- NUEVO: Estado para controlar si vemos el Inicio o el Catálogo ---
    const [mostrarCatalogo, setMostrarCatalogo] = useState(false);

    // --- LÓGICA DE VIDEO ---
    const reproducirTrailer = (movie) => {
        if (trailerUrl) {
            setTrailerUrl("");
        } else {
            movieTrailer(movie?.titulo || "")
                .then((url) => {
                    const urlParams = new URLSearchParams(new URL(url).search);
                    setTrailerUrl(urlParams.get("v"));
                })
                .catch((error) => console.log("No se encontró trailer", error));
        }
    };

    const opts = { height: "100%", width: "100%", playerVars: { autoplay: 1 } };

    // --- LÓGICA DE LISTA ---
    const toggleMiLista = (movie) => {
        const yaEsta = miLista.find(m => m.id === movie.id);
        let nuevaLista;
        if (yaEsta) nuevaLista = miLista.filter(m => m.id !== movie.id);
        else nuevaLista = [...miLista, movie];
        setMiLista(nuevaLista);
        localStorage.setItem("miListaJavaflix", JSON.stringify(nuevaLista));
    };

    // --- FUNCIONES VISUALES ---
    const getImage = (peli) => {
        if (!peli) return "https://via.placeholder.com/300x450?text=No+Data";
        if (peli.urlImagen && peli.urlImagen.startsWith("http")) return peli.urlImagen;
        return `https://picsum.photos/seed/${peli.id}/300/450`;
    };
    const getBackdrop = (peli) => {
        if (!peli) return "https://via.placeholder.com/1920x1080?text=No+Image";
        if (peli.urlImagen && peli.urlImagen.startsWith("http")) return peli.urlImagen;
        return `https://picsum.photos/seed/${peli.id}/1920/1080`;
    };

    // --- HANDLERS ---
    const handleMovieClick = (movie) => {
        setSelectedMovie(movie);
        document.body.style.overflow = 'hidden';
    };
    const closeModal = () => {
        setSelectedMovie(null);
        document.body.style.overflow = 'auto';
    };
    const closeVideo = () => { setTrailerUrl(""); }

    useEffect(() => {
        async function fetchData() {
            try {
                const req = await axios.get('http://localhost:8081/api/peliculas');
                const data = req.data;
                setPeliculas(data);
                if (data && data.length > 0) {
                    const random = Math.floor(Math.random() * data.length);
                    setPeliculaHero(data[random]);
                }
            } catch (error) { console.error(error); }
        }
        fetchData();
        const handleScroll = () => {
            if (window.scrollY > 100) setNavbarBlack(true);
            else setNavbarBlack(false);
        };
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    const peliculasFiltradas = peliculas.filter(p => p.titulo && p.titulo.toLowerCase().includes(busqueda.toLowerCase()));
    const topRated = peliculas.filter(p => p.valoracion >= 8);
    const recientes = peliculas.filter(p => p.fechaEstreno && new Date(p.fechaEstreno) > new Date('2020-01-01'));
    const estaEnMiLista = selectedMovie && miLista.some(m => m.id === selectedMovie.id);

    return (
        <div className="App">
            <div className={`navbar ${navbarBlack && "black"}`}>
                <div className="logo" onClick={() => setMostrarCatalogo(false)}>ManuFlix</div>

                {/* --- NUEVO: Menú de navegación (Inicio | Catálogo) --- */}
                <div className="nav-links">
                    <span
                        className={`nav-link ${!mostrarCatalogo ? 'active' : ''}`}
                        onClick={() => setMostrarCatalogo(false)}
                    >
                        Inicio
                    </span>
                    <span
                        className={`nav-link ${mostrarCatalogo ? 'active' : ''}`}
                        onClick={() => setMostrarCatalogo(true)}
                    >
                        Catálogo
                    </span>
                </div>

                <div className="search-box">
                    <FaSearch style={{marginRight: '10px'}} />
                    <input type="text" placeholder="Buscar..." onChange={(e) => setBusqueda(e.target.value)} />
                </div>
            </div>

            {/* --- HERO BANNER (Solo se muestra en Inicio y si no buscas) --- */}
            {!mostrarCatalogo && peliculaHero && !busqueda && (
                <header className="banner" style={{ backgroundImage: `url("${getBackdrop(peliculaHero)}")` }}>
                    <div className="banner__contents">
                        <h1 className="banner__title">{peliculaHero.titulo}</h1>
                        <div className="banner__buttons">
                            <button className="banner__button" onClick={() => reproducirTrailer(peliculaHero)}>
                                <FaPlay /> Reproducir
                            </button>
                            <button className="banner__button" onClick={() => handleMovieClick(peliculaHero)}>
                                <FaInfoCircle /> Más info
                            </button>
                        </div>
                        <h1 className="banner__description">
                            {peliculaHero.sinopsis ? (peliculaHero.sinopsis.length > 150 ? peliculaHero.sinopsis.substring(0, 150) + "..." : peliculaHero.sinopsis) : "Sinopsis no disponible"}
                        </h1>
                    </div>
                    <div className="banner--fadeBottom" />
                </header>
            )}

            {/* --- CONTENIDO PRINCIPAL --- */}
            <div style={{marginTop: (busqueda || mostrarCatalogo) ? '100px' : '-100px', position: 'relative', zIndex: 10, paddingBottom: '50px'}}>

                {/* CASO 1: BÚSQUEDA (Prioridad máxima) */}
                {busqueda ? (
                    <div style={{padding: '0 20px'}}>
                        <h2>Resultados: {busqueda}</h2>
                        <div style={{display: 'flex', flexWrap: 'wrap', gap: '20px', justifyContent: 'center'}}>
                            {peliculasFiltradas.map(p => (
                                <div key={p.id} className="movie-card" onClick={() => handleMovieClick(p)}>
                                    <img src={getImage(p)} alt={p.titulo} className="movie-img" />
                                    <div className="movie-title">{p.titulo}</div>
                                </div>
                            ))}
                        </div>
                    </div>

                    // CASO 2: CATÁLOGO COMPLETO (Si pulsaste el botón)
                ) : mostrarCatalogo ? (
                    <div style={{padding: '0 40px'}}>
                        <h2 style={{marginBottom: '20px'}}>Catálogo Completo ({peliculas.length})</h2>
                        <div style={{display: 'flex', flexWrap: 'wrap', gap: '20px', justifyContent: 'flex-start'}}>
                            {peliculas.map(p => (
                                <div key={p.id} className="movie-card" onClick={() => handleMovieClick(p)}>
                                    <img src={getImage(p)} alt={p.titulo} className="movie-img" />
                                    <div className="movie-title">{p.titulo}</div>
                                </div>
                            ))}
                        </div>
                    </div>

                    // CASO 3: VISTA INICIO (Con filas y scroll)
                ) : (
                    <>
                        <Row title="Originales de Javaflix" movies={peliculas} isLargeRow={true} getImage={getImage} onMovieClick={handleMovieClick} />
                        <Row title="Tendencias" movies={topRated} getImage={getImage} onMovieClick={handleMovieClick} />
                        {miLista.length > 0 && <Row title="Mi Lista" movies={miLista} getImage={getImage} onMovieClick={handleMovieClick} />}
                        <Row title="Novedades" movies={recientes} getImage={getImage} onMovieClick={handleMovieClick} />
                    </>
                )}
            </div>

            {/* --- VIDEO OVERLAY --- */}
            {trailerUrl && (
                <div className="video-overlay" onClick={closeVideo}>
                    <div className="video-container">
                        <YouTube videoId={trailerUrl} opts={opts} />
                        <button className="video-close-btn" onClick={closeVideo}>Cerrar X</button>
                    </div>
                </div>
            )}

            {/* --- MODAL --- */}
            {selectedMovie && (
                <div className="modal-overlay" onClick={closeModal}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <button className="modal-close-btn" onClick={closeModal}><FaTimes size={20}/></button>
                        <div className="modal-hero" style={{ backgroundImage: `url("${getBackdrop(selectedMovie)}")` }}>
                            <div className="modal-hero-fade" />
                            <div className="modal-hero-content">
                                <h1 className="modal-title-text">{selectedMovie.titulo}</h1>
                                <div className="modal-btns">
                                    <button className="btn-play" onClick={() => reproducirTrailer(selectedMovie)}>
                                        <FaPlay size={15} /> Reproducir
                                    </button>
                                    <button
                                        className="btn-circle"
                                        onClick={() => toggleMiLista(selectedMovie)}
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
                                    <span>{(selectedMovie.fechaEstreno || "2024").split('-')[0]}</span>
                                    <span className="age-badge">16+</span>
                                    <span>{selectedMovie.duracion ? `${selectedMovie.duracion} min` : "1h 30m"}</span>
                                    <span style={{border:'1px solid white', fontSize:'0.7rem', padding:'0 3px'}}>HD</span>
                                </div>
                                <p className="modal-description">{selectedMovie.sinopsis || "No hay descripción disponible."}</p>
                            </div>
                            <div className="modal-col-right">
                                <div className="modal-details-item"><span className="label-gray">Reparto: </span><span className="link-white">Actores desconocidos</span></div>
                                <div className="modal-details-item"><span className="label-gray">Géneros: </span><span className="link-white">Acción, Drama</span></div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

// Componente Row (Igual que antes, con scroll)
function Row({ title, movies, isLargeRow, getImage, onMovieClick }) {
    if(!movies) return null;
    const handleWheel = (e) => {
        if (e.deltaY === 0) return;
        e.currentTarget.scrollLeft += e.deltaY;
    };
    return (
        <div className="row">
            <h2>{title}</h2>
            <div className="row__posters" onWheel={handleWheel}>
                {movies.map(movie => (
                    <div key={movie.id} className={`movie-card ${isLargeRow ? "large" : ""}`} onClick={() => onMovieClick(movie)}>
                        <img className="movie-img" src={getImage(movie)} alt={movie.titulo} onError={(e) => { e.target.src = "https://via.placeholder.com/300x450?text=Error"; }} />
                        <div className="movie-title">{movie.titulo}</div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default App;