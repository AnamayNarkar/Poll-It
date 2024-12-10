import React, { useState } from 'react'
import '../styles/LoginForm.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../../../services/ApiRequests/authRequests';

const LoginForm = ({ setIsLoginFormCustom }) => {

    const navigate = useNavigate();

    const [loginForm, setLoginForm] = useState({
        username: '',
        password: ''
    });

    const handleInputChange = (e) => {
        setLoginForm({
            ...loginForm,
            [e.target.name]: e.target.value
        });
    }

    const handleFormSubmit = async (e) => {
        e.preventDefault();

        loginForm.username = loginForm.username.trim();
        loginForm.password = loginForm.password.trim();

        const response = await loginRequest(loginForm);
        navigate('/home');
    }

    return (
        <div className='loginFormContainer'>
            <h1 className='loginFormTitle'>Login</h1>

            <form className='loginForm'>
                <input type='text' name='username' placeholder='Username' value={loginForm.username} onChange={handleInputChange} className='loginFormInput' maxLength='20' />
                <input type='password' name='password' placeholder='Password' value={loginForm.password} onChange={handleInputChange} className='loginFormInput' maxLength='20' />
                <button type='submit' className='loginFormButton' onClick={handleFormSubmit}>Login</button>
            </form>

            <h3 className='loginFormSwitchForm' onClick={setIsLoginFormCustom} style={{ cursor: 'pointer', color: 'white', fontFamily: '"Parkinsans", sans-serif' }}>
                Don't have an account?
            </h3>
        </div>
    )
}

export default LoginForm
