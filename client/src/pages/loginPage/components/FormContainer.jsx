import React, { useState } from 'react'
import '../styles/FormContainer.css'

import LoginForm from './LoginForm'
import RegistrationForm from './RegistrationForm'

const FormContainer = () => {

    const [isLoginForm, setIsLoginForm] = useState(true);

    function setIsLoginFormCustom() {
        setIsLoginForm(!isLoginForm);
    }

    return (
        <div className='formContaniner'>
            {isLoginForm ? <LoginForm setIsLoginFormCustom={setIsLoginFormCustom} /> : <RegistrationForm setIsLoginFormCustom={setIsLoginFormCustom} />}
        </div>
    )
}

export default FormContainer;
