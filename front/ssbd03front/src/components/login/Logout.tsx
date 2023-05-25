import * as React from 'react';
import {useCookies} from "react-cookie";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

const Logout = () => {

    const navigate = useNavigate();
    const [cookies, setCookie, removeCookie] = useCookies(["token", "role"]);
    useEffect(() => {
        // removeCookie('role');
        localStorage.removeItem("token")
        navigate("/");
    }, []);
    return (<p></p>)

}
export default Logout;