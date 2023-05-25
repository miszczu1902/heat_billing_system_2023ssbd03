import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {API_URL} from "../../consts";
import {Container, Grid, Typography} from "@mui/material";
import logo from "../../assets/logo.svg";
import {useTranslation} from "react-i18next";
import Paper from "@mui/material/Paper";

const ConfirmEmail = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = "Bearer " + localStorage.getItem("token");
    const {activationToken} = useParams<{ activationToken: string }>();
    const [message, setMessage] = useState('');
    const [isActivated, setIsActivated] = useState(false);

    const handleButtonClick = (path: string) => {
        navigate(path);
    }

    useEffect(() => {
        axios.patch(`${API_URL}/accounts/self/confirm-new-email`,
            {activationToken: activationToken},
            {
                headers: {
                    'Authorization': token,
                    'Content-Type': 'application/json'
                },
            }).then(() => {
            setMessage(t('email.confirmed'));
            setIsActivated(true)
        });
    }, []);

    return (<div className="landing-page-root">
        <Container>
            <img src={logo} alt="Logo"/>
        </Container>
        <Container maxWidth="sm">
            <Grid container direction="column" alignItems="center" spacing={4}>
                <Grid item>
                    <Typography variant="h4" component="h1">{t(message)}</Typography>
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