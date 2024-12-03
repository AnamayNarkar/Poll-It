import React, { useState } from 'react'
import axios from 'axios';
import '../styles/RegistrationForm.css'

const RegistrationForm = ({ setIsLoginFormCustom }) => {

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
        const response = await axios.post('http://localhost:3000/api/user/register', registrationFormData, { withCredentials: true });
        if (response.status >= 200 && response.status < 300) {
            navigate('/home');
        } else {
            window.alert(response.data.message);
        }
    }

    return (
        <div className='registrationFormContainer'>

            <h1 className='registrationFormTitle'>Register</h1>

            <form className='registrationForm'>

                <input type='text' name='username' placeholder='Username' className='registrationFormInput' onChange={handleRegistrationFormChange} />
                <input type='email' name='email' placeholder='Email' className='registrationFormInput' onChange={handleRegistrationFormChange} />
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
