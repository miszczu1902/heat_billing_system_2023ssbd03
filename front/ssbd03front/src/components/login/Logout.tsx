import * as React from 'react';
import {useEffect} from 'react';
import {useNavigate} from "react-router-dom";

const Logout = () => {

    const navigate = useNavigate();
    useEffect(() => {
        localStorage.removeItem("role");
        localStorage.removeItem("token");
        navigate("/");
    }, []);
    return (<p></p>)

}
export default Logout;