import * as React from 'react';
import {useCookies} from "react-cookie";

import {useNavigate} from "react-router-dom";


export default function Logout() {
    const [cookies, setCookie, removeCookie] = useCookies(["token","role"]);
    removeCookie('role');
    removeCookie("token",{path: '/'});
    window.location.reload();
    return( <p></p>)


}
