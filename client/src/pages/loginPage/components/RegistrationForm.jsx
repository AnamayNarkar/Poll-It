import React, { useState } from 'react'
import axios from 'axios';
import '../styles/RegistrationForm.css'
import registrationRequest from '../../../services/ApiRequests/registrationRequest';
import { useNavigate } from 'react-router-dom';

const RegistrationForm = ({ setIsLoginFormCustom }) => {

    const navigate = useNavigate();

    const [registrationFormData, setRegistrationFormData] = React.useState({
        username: '',
        email: '',
        password: ''
    });

    const handleRegistrationFormChange = (e) => {
        setRegistrationFormData({
            ...registrationFormData,
            [e.target.name]: e.target.value
        });
    }

    const handleRegistrationFormSubmit = async (e) => {
        e.preventDefault();
        const response = await registrationRequest(registrationFormData);
        navigate('/home');
    }

    return (
        <div className='registrationFormContainer'>

            <h1 className='registrationFormTitle'>Register</h1>

            <form className='registrationForm'>
                <input type='email' name='email' placeholder='Email' className='registrationFormInput' onChange={handleRegistrationFormChange} />
                <input type='text' name='username' placeholder='Username' className='registrationFormInput' onChange={handleRegistrationFormChange} />
                <input type='password' name='password' placeholder='Password' className='registrationFormInput' onChange={handleRegistrationFormChange} />
                <button type='submit' className='registrationFormButton' onClick={handleRegistrationFormSubmit}>Register</button>
            </form>

            <h3 className='registrationFormSwitchForm' onClick={setIsLoginFormCustom} style={{ cursor: 'pointer', color: 'white', fontFamily: '"Parkinsans", sans-serif' }}>
                Already have an account?
            </h3>

        </div>
    )
}

export default RegistrationForm
