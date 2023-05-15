import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {API_URL} from "../../consts";

const ActivateFromEmail = () => {
    const {activationToken} = useParams<{ activationToken: string }>();
    const [message, setMessage] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            axios.post(`${API_URL}/accounts/activate-from-email`,
                {activationToken: activationToken}
            ).then(() => {
                setMessage('Konto aktywowane');
            }).catch(error => setMessage(error.reason.message));
        };
        fetchData();
    });

    return (<p>{message}</p>);
}
export default ActivateFromEmail;