import * as React from 'react';
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";

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