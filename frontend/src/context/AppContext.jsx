import { createContext, useState, useEffect, useContext } from 'react';
import axios from 'axios';

const AppContext = createContext();

export function AppProvider({ children }) {
    const [miLista, setMiLista] = useState(() => {
        const guardado = localStorage.getItem("miListaJavaflix");
        return guardado ? JSON.parse(guardado) : [];
    });

    const toggleMiLista = (movie) => {
        const yaEsta = miLista.find(m => m.id === movie.id);
        let nuevaLista;
        if (yaEsta) {
            nuevaLista = miLista.filter(m => m.id !== movie.id);
        } else {
            nuevaLista = [...miLista, movie];
        }
        setMiLista(nuevaLista);
        localStorage.setItem("miListaJavaflix", JSON.stringify(nuevaLista));
    };

    const isEnMiLista = (movieId) => {
        return miLista.some(m => m.id === movieId);
    };

    // UI Global State
    const [busqueda, setBusqueda] = useState("");
    const [selectedMovie, setSelectedMovie] = useState(null);
    const [trailerUrl, setTrailerUrl] = useState("");

    // --- USER PROFILES ---
    const [profiles, setProfiles] = useState([]);

    // Load profiles from Backend
    useEffect(() => {
        axios.get('http://localhost:8081/api/usuarios')
            .then(response => {
                const mappedProfiles = response.data.map(u => ({
                    id: u.id,
                    name: u.username,
                    avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${u.username}`,
                    rol: u.rol
                }));
                setProfiles(mappedProfiles);
            })
            .catch(error => console.error("Error loading profiles:", error));
    }, []);

    const [currentProfile, setCurrentProfile] = useState(null);

    // We removed localStorage persistence for currentProfile to force selection on reload
    // useEffect(() => {
    //     if (currentProfile) {
    //         localStorage.setItem("javaflix_current_profile", JSON.stringify(currentProfile));
    //     } else {
    //         localStorage.removeItem("javaflix_current_profile");
    //     }
    // }, [currentProfile]);

    const addProfile = (name) => {
        // Create user in backend
        // We send email/password as dummy values since user didn't want full security
        const newUser = {
            username: name,
            email: `${name.toLowerCase().replace(/\s/g, '')}@cine.com`,
            password: "1234", // Dummy password
            rol: "USER"
        };

        axios.post('http://localhost:8081/api/usuarios', newUser)
            .then(response => {
                const u = response.data;
                const newProfile = {
                    id: u.id,
                    name: u.username,
                    avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${u.username}`,
                    rol: u.rol
                };
                setProfiles([...profiles, newProfile]);
            })
            .catch(error => {
                console.error("Error creating profile:", error);
                alert("Error al crear usuario (quizás el nombre ya existe)");
            });
    };

    const deleteProfile = (id) => {
        axios.delete(`http://localhost:8081/api/usuarios/${id}`)
            .then(() => {
                const newProfiles = profiles.filter(p => p.id !== id);
                setProfiles(newProfiles);
                if (currentProfile && currentProfile.id === id) {
                    setCurrentProfile(null);
                }
            })
            .catch(error => console.error("Error deleting profile:", error));
    };

    return (
        <AppContext.Provider value={{
            miLista, toggleMiLista, isEnMiLista,
            busqueda, setBusqueda,
            selectedMovie, setSelectedMovie,
            trailerUrl, setTrailerUrl,
            profiles, currentProfile, setCurrentProfile, addProfile, deleteProfile
        }}>
            {children}
        </AppContext.Provider>
    );
}

export function useAppContext() {
    return useContext(AppContext);
}
