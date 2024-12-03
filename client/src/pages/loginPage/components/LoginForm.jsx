import React, { useState } from 'react'
import '../styles/LoginForm.css'

const LoginForm = ({ setIsLoginFormCustom }) => {

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

    return (
        <div className='loginFormContainer'>
            <h1 className='loginFormTitle'>Login</h1>

            <form className='loginForm'>

                <input type='text' name='username' placeholder='Username' value={loginForm.username} onChange={handleInputChange} className='loginFormInput' />
                <input type='password' name='password' placeholder='Password' value={loginForm.password} onChange={handleInputChange} className='loginFormInput' />

                <button type='submit' className='loginFormButton'>Login</button>

            </form>

            <h3 className='loginFormSwitchForm' onClick={setIsLoginFormCustom} style={{ cursor: 'pointer', color: 'white', fontFamily: '"Parkinsans", sans-serif' }}>
                Don't have an account?
            </h3>
        </div>
    )
}

export default LoginForm
