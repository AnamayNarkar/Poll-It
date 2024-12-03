import React, { useState } from 'react'
import '../styles/LoginForm.css'
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

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
        const response = await axios.post('/api/user/login', loginForm);
        if (response.status >= 200 && response.status < 300) {

            navigate('/home');
        } else {
            window.alert('Invalid credentials');
            console.log(response);
            loginForm.username = '';
            loginForm.password = '';
        }
    }

    return (
        <div className='loginFormContainer'>
            <h1 className='loginFormTitle'>Login</h1>

            <form className='loginForm'>

                <input type='text' name='username' placeholder='Username' value={loginForm.username} onChange={handleInputChange} className='loginFormInput' />
                <input type='password' name='password' placeholder='Password' value={loginForm.password} onChange={handleInputChange} className='loginFormInput' />

                <button type='submit' className='loginFormButton' onClick={handleFormSubmit}>Login</button>

            </form>

            <h3 className='loginFormSwitchForm' onClick={setIsLoginFormCustom} style={{ cursor: 'pointer', color: 'white', fontFamily: '"Parkinsans", sans-serif' }}>
                Don't have an account?
            </h3>
        </div>
    )
}

export default LoginForm
