import * as React from 'react';
import {useCookies} from "react-cookie";

const Logout = () => {
    const [cookies, setCookie, removeCookie] = useCookies(["token", "role"]);
    removeCookie('role');
    removeCookie("token", {path: '/'});
    window.location.reload();
    return (<p></p>)
}
export default Logout;