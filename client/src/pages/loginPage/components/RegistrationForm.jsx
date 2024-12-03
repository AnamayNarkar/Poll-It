import React from 'react'
import '../styles/RegistrationForm.css'

const RegistrationForm = ({ setIsLoginFormCustom }) => {
    return (
        <div className='registrationFormContainer'>

            <h1 className='registrationFormTitle'>Register</h1>

            <form className='registrationForm'>

                <input type='text' name='username' placeholder='Username' className='registrationFormInput' />
                <input type='email' name='email' placeholder='Email' className='registrationFormInput' />
                <input type='password' name='password' placeholder='Password' className='registrationFormInput' />

                <button type='submit' className='registrationFormButton'>Register</button>
            </form>

            <h3 className='registrationFormSwitchForm' onClick={setIsLoginFormCustom} style={{ cursor: 'pointer', color: 'white', fontFamily: '"Parkinsans", sans-serif' }}>
                Already have an account?
            </h3>

        </div>
    )
}

export default RegistrationForm
