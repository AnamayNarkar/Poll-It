import React, { useState } from 'react';
import '../styles/CreateTagsContainerStyles.css';
import createTagRequest from '../../../services/ApiRequests/createTagRequest';

const CreateTagsContainer = () => {
    const [tagName, setTagName] = useState('');
    const [statusMessage, setStatusMessage] = useState('');

    const handleCreateTag = async () => {
        const response = await createTagRequest(tagName);

        setTagName('');

        if (response.status >= 200 && response.status < 300) {
            setStatusMessage('Tag created successfully');
        } else {
            setStatusMessage(response.data.message || 'An error occurred');
        }

        setTimeout(() => {
            setStatusMessage('');
        }, 3000);

    };

    return (
        <div className='createTagsContainer'>
            <div className='inputAndButtonContainerForCreateTags'>
                <input
                    type='text'
                    placeholder='Create a tag'
                    className='inputForCreateTags'
                    value={tagName}
                    onChange={(e) => setTagName(e.target.value)}
                />
                <button className='buttonForCreateTags' onClick={handleCreateTag}>
                    Create
                </button>
            </div>
            {statusMessage && statusMessage === 'Tag created successfully' ? (<h2 className='statusMessageForCreateTags' style={{
                color: 'rgb(42, 228, 42)',
                fontFamily: 'Parkinsans , sans-serif'

            }}>{statusMessage}</h2>) : (<h2 className='statusMessageForCreateTags' style={{
                color: 'red', fontFamily: 'Parkinsans , sans-serif'

            }}>{statusMessage}</h2>)}
        </div>
    );
};

export default CreateTagsContainer;
