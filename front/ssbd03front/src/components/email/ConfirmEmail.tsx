import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {API_URL} from "../../consts";
import {Container, Grid, Typography} from "@mui/material";
import logo from "../../assets/logo.svg";
import {useTranslation} from "react-i18next";
import Paper from "@mui/material/Paper";
import {useCookies} from "react-cookie";
import jwt from "jwt-decode";

const ConfirmEmail = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);
    const [role, setRole] = useState('');
    const token = "Bearer " + cookies.token;
    const {activationToken} = useParams<{ activationToken: string }>();
    const [message, setMessage] = useState('');
    const [isActivated, setIsActivated] = useState(false);

    const handleButtonClick = (path: string) => {
        navigate(path);
    }

    const fetchData = async () => {
        axios.patch(`${API_URL}/accounts/self/confirm-new-email`,
            {activationToken: activationToken},
            {
                headers: {
                    'Authorization': token,
                    'Content-Type': 'application/json'
                },
            }).then(() => {
            setMessage("Mail został potwierdzony!");
            setIsActivated(true)
        }).catch(error => {
            setMessage(error.reason.message);
            if (error.response.status == 403) navigate('/');
        });
    };

    useEffect(() => {
        if (cookies.token !== "undefined" && cookies.token !== undefined) {
            const decodedToken = jwt(cookies.token);
            const decodedRole = JSON.parse(JSON.stringify(decodedToken)).role;
            setRole(decodedRole.split(','));
            const currentTimestamp = Math.floor(new Date().getTime() / 1000);
            if (JSON.parse(JSON.stringify(decodedToken)).exp < currentTimestamp) {
                removeCookie('token');
                navigate('');
            }
            fetchData();
        } else {
            navigate('/');
        }
    }, [cookies.token]);

    return (<div className="landing-page-root">
        <Container>
            <img src={logo} alt="Logo"/>
        </Container>
        <Container maxWidth="sm">
            <Grid container direction="column" alignItems="center" spacing={4}>
                <Grid item>
                    <Typography variant="h4" component="h1">{message}</Typography>
                </Grid>
                {isActivated &&
                    <Grid item>
                        <Paper elevation={3} style={{position: 'relative'}}>
                            <Typography sx={{padding: '1vh'}}
                                        variant="h5"><b>{t('email.confirmed')}</b>
                            </Typography>
                        </Paper>
                    </Grid>
                }
            </Grid>
        </Container>
    </div>);
}

export default ConfirmEmail;