import React, { useCallback } from 'react'
import { useDropzone } from 'react-dropzone'
import axios from 'axios';

function MyDropzone({ userProfileId }) {
    const onDrop = useCallback(acceptedFiles => {
        // Do something with the files
        const file = acceptedFiles[0];
        console.log(file);


        // take this file and send it to the backend 
        // call the backendService
        const formData = new FormData();
        formData.append("file", file);

        axios.post(
            `http://localhost:8086/api/v1/users/${userProfileId}/image/upload`,
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            }
        ).then(() => {
            console.log("file uploaded")
        }).catch(err => {
            console.log(err)
        })

    }, [])
    const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop })

    return (
        <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the files here ...</p> :
                    <p>Drag 'n' drop some files here, or click to select files</p>
            }
        </div>
    )
}

export default MyDropzone;