import { BrowserRouter as Router, Routes, Route, useLocation, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import movieTrailer from 'movie-trailer';
import Navbar from './components/Navbar/Navbar';
import Home from './pages/Home';
import Catalog from './pages/Catalog';
import Modal from './components/Modal/Modal';
import VideoPlayer from './components/VideoPlayer/VideoPlayer';
import ProfileSelection from './pages/ProfileSelection';
import UserManagement from './pages/UserManagement';
import MovieManagement from './pages/MovieManagement';
import { AppProvider, useAppContext } from './context/AppContext';
import './index.css';
import './App.css';

// Protected Route Component
function ProtectedRoute({ children }) {
    const { currentProfile } = useAppContext();
    if (!currentProfile) {
        return <Navigate to="/profiles" replace />;
    }
    return children;
}

// Admin Route Component
function AdminRoute({ children }) {
    const { currentProfile } = useAppContext();
    if (!currentProfile || currentProfile.rol !== 'ADMIN') {
        return <Navigate to="/" replace />;
    }
    return children;
}

function AppContent() {
    const {
        selectedMovie, setSelectedMovie,
        trailerUrl, setTrailerUrl,
        setBusqueda, currentProfile
    } = useAppContext();

    const [navbarBlack, setNavbarBlack] = useState(false);
    const location = useLocation();

    // Only show Navbar if we are NOT on the profile screen
    const showNavbar = location.pathname !== '/profiles';

    useEffect(() => {
        const handleScroll = () => {
            if (window.scrollY > 100) setNavbarBlack(true);
            else setNavbarBlack(false);
        };
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    useEffect(() => {
        setBusqueda("");
        window.scrollTo(0, 0);
    }, [location.pathname, setBusqueda]);

    return (
        <div className="App">
            {showNavbar && (
                <Navbar
                    navbarBlack={navbarBlack}
                    setBusqueda={setBusqueda}
                />
            )}

            <Routes>
                <Route path="/profiles" element={<ProfileSelection />} />

                <Route path="/" element={
                    <ProtectedRoute>
                        <Home />
                    </ProtectedRoute>
                } />

                <Route path="/catalog" element={
                    <ProtectedRoute>
                        <Catalog />
                    </ProtectedRoute>
                } />

                <Route path="/admin/users" element={
                    <AdminRoute>
                        <UserManagement />
                    </AdminRoute>
                } />

                <Route path="/admin/movies" element={
                    <AdminRoute>
                        <MovieManagement />
                    </AdminRoute>
                } />
            </Routes>

            {/* Global Overlays */}
            {selectedMovie && (
                <Modal
                    movie={selectedMovie}
                    onClose={() => {
                        setSelectedMovie(null);
                        document.body.style.overflow = 'auto';
                    }}
                    onPlay={(movie) => {
                        movieTrailer(movie?.titulo || "")
                            .then((url) => {
                                const urlParams = new URLSearchParams(new URL(url).search);
                                setTrailerUrl(urlParams.get("v"));
                                setSelectedMovie(null); // Close modal to show video
                            })
                            .catch((error) => console.log("No se encontró trailer", error));
                    }}
                />
            )}

            {trailerUrl && (
                <VideoPlayer
                    trailerUrl={trailerUrl}
                    onClose={() => setTrailerUrl("")}
                />
            )}
        </div>
    );
}

function App() {
    return (
        <AppProvider>
            <Router>
                <AppContent />
            </Router>
        </AppProvider>
    );
}

export default App;