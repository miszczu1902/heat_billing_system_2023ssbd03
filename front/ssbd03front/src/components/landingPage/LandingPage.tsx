import {Button, Container, Grid, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import logo from './../../assets/logo.svg';
import React, {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import NavbarPanel from "../navigation/NavbarPanel";

const LandingPage = () => {
    const navigate = useNavigate();
    const [loggedIn, setLoggedIn] = useState(false);
    const [cookies, setCookie] = useCookies(["token"]);

    const handleButtonClick = (path: string) => {
        navigate(path);
    }

    useEffect(() => {
        if (cookies.token) setLoggedIn(true);
    });

    return (
        <div>
            {loggedIn && <NavbarPanel/>}
            <div className="landing-page-root">
                <Container>
                    <img src={logo} alt="Logo"/>
                </Container>
                <Container maxWidth="sm">
                    <Grid container direction="column" alignItems="center" spacing={4}>
                        <Grid item>
                            <Typography variant="h4" component="h1">
                                Projekt zespołu 3 SSBD 2023
                            </Typography>
                        </Grid>
                        <Grid item>
                            <Typography variant="subtitle1">
                                System rozliczania ciepła w lokalach w wielu budynkach.
                            </Typography>
                        </Grid>
                        {!loggedIn &&
                            <Grid item>
                                <Button className="landing-page-button" variant="contained" color="primary"
                                        onClick={() => handleButtonClick('/register')}>
                                    Zarejestruj się
                                </Button>
                                <Button className="landing-page-button" variant="contained" color="primary"
                                        onClick={() => handleButtonClick('/login')}>
                                    Zaloguj się
                                </Button>
                            </Grid>
                        }
                    </Grid>
                </Container>
            </div>
        </div>
    );
}
export default LandingPage;