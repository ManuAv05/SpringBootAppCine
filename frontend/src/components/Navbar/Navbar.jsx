import { Link, useLocation } from 'react-router-dom';
import { FaSearch } from 'react-icons/fa';
import { useAppContext } from '../../context/AppContext';
import './Navbar.css';

function Navbar({ navbarBlack, setBusqueda, setMostrarCatalogo }) {
    const location = useLocation();
    const { currentProfile, setCurrentProfile } = useAppContext();
    const isCatalog = location.pathname === "/catalog";
    const isHome = location.pathname === "/";

    return (
        <div className={`navbar ${navbarBlack ? "black" : ""}`}>
            <Link to="/" className="logo" onClick={() => { if (setMostrarCatalogo) setMostrarCatalogo(false); }}>
                ManuFlix
            </Link>

            <div className="nav-links">
                <Link
                    to="/"
                    className={`nav-link ${isHome ? 'active' : ''}`}
                >
                    Inicio
                </Link>
                <Link
                    to="/catalog"
                    className={`nav-link ${isCatalog ? 'active' : ''}`}
                >
                    Catálogo
                </Link>

                {/* Only show Admin link if user has ADMIN role */}
                {currentProfile && currentProfile.rol === 'ADMIN' && (
                    <>
                        <Link
                            to="/admin/users"
                            className={`nav-link ${location.pathname === '/admin/users' ? 'active' : ''}`}
                        >
                            Usuarios
                        </Link>
                        <Link
                            to="/admin/movies"
                            className={`nav-link ${location.pathname === '/admin/movies' ? 'active' : ''}`}
                        >
                            Películas
                        </Link>
                    </>
                )}

                <Link
                    to="/profiles"
                    className="nav-link"
                    onClick={() => setCurrentProfile(null)}
                >
                    Cambiar Perfil
                </Link>
            </div>

            <div className="search-box">
                <FaSearch style={{ marginRight: '10px' }} />
                <input
                    type="text"
                    placeholder="Buscar..."
                    onChange={(e) => setBusqueda(e.target.value)}
                />
            </div>
        </div>
    );
}

export default Navbar;
