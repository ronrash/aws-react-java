import React, { useState, useEffect } from 'react'
import axios from 'axios';
import MyDropZone from './MyDropZone'

const url = 'http://localhost:8086/api/v1/users';

const MainApp = () => {

    const [userProfiles, setUserProfiles] = useState([]);

    const fetchData = async () => {
        try {
            const response = await axios(url);
            const data = response.data;
            setUserProfiles(data)
        } catch (error) {
            console.log(error)
        }
    }

    useEffect(() => {
        fetchData();
    }, [])

    const Id = 'Id'

    return (
        <div>

            {userProfiles.map((userProfile) => {
                const { userName, userProfileId, amazonS3link } = userProfile;
                return (
                    <div key={userProfileId}>
                        {userProfile.userProfileId ? <img
                            src={`http://localhost:8086/api/v1/users/${userProfileId}/image/download`} /> : null}
                        <br></br>
                        <br></br>
                        <p>{userProfileId}</p>
                        <p>{userName}</p>
                        <p>{amazonS3link}</p>
                        <MyDropZone  {...userProfile} />
                        <br></br>
                    </div>
                )
            })}
        </div >
    )
}

export default MainApp
