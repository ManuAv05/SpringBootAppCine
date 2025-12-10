import { useState } from 'react';
import { useAppContext } from '../context/AppContext';
import { useNavigate } from 'react-router-dom';
import { FaPlusCircle } from 'react-icons/fa';
import './ProfileSelection.css';

function ProfileSelection() {
    const { profiles, setCurrentProfile, addProfile, deleteProfile } = useAppContext();
    const navigate = useNavigate();
    const [isAdding, setIsAdding] = useState(false);
    const [newName, setNewName] = useState("");

    const handleProfileClick = (profile) => {
        setCurrentProfile(profile);
        navigate('/');
    };

    const handleAdd = () => {
        if (newName.trim()) {
            addProfile(newName.trim());
            setNewName("");
            setIsAdding(false);
        }
    };

    return (
        <div className="profile-screen">
            <h1 className="profile-title">
                {isAdding ? "Añadir Perfil" : "¿Quién está viendo ahora?"}
            </h1>

            {!isAdding ? (
                <div className="profile-list">
                    {profiles.map(profile => (
                        <div
                            key={profile.id}
                            className="profile-item"
                            onClick={() => handleProfileClick(profile)}
                        >
                            <div className="avatar-wrapper">
                                <img src={profile.avatar} alt={profile.name} className="profile-avatar" />
                            </div>
                            <span className="profile-name">{profile.name}</span>
                        </div>
                    ))}

                    {profiles.length < 5 && (
                        <div className="profile-item" onClick={() => setIsAdding(true)}>
                            <div className="avatar-wrapper add-btn">
                                <FaPlusCircle size={50} color="#666" />
                            </div>
                            <span className="profile-name">Añadir Perfil</span>
                        </div>
                    )}
                </div>
            ) : (
                <div className="add-profile-form">
                    <div className="form-group">
                        <img
                            src={`https://api.dicebear.com/7.x/avataaars/svg?seed=${newName || 'new'}`}
                            alt="Preview"
                            className="profile-avatar preview"
                        />
                        <input
                            type="text"
                            placeholder="Nombre del perfil"
                            value={newName}
                            onChange={(e) => setNewName(e.target.value)}
                            autoFocus
                        />
                    </div>
                    <div className="form-buttons">
                        <button className="save-btn" onClick={handleAdd}>Guardar</button>
                        <button className="cancel-btn" onClick={() => setIsAdding(false)}>Cancelar</button>
                    </div>
                </div>
            )
            }
        </div>
    );
}

export default ProfileSelection;
