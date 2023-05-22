import {Button, Container, Grid, Icon, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {useCookies} from "react-cookie";
import NavbarPanel from "../navigation/NavbarPanel";
import Logo from "../../assets/logo.svg";
import {useTranslation} from "react-i18next";
import {GUEST} from "../../consts";

const LandingPage = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const [loggedIn, setLoggedIn] = useState(false);
    const [cookies, setCookie] = useCookies(["token", "role"]);

    const handleButtonClick = (path: string) => {
        navigate(path);
    }

    useEffect(() => {
        if (cookies.token) setLoggedIn(true);
        else setCookie('role', GUEST);
    });

    return (
        <div>
            <NavbarPanel/>
            <div className="landing-page-root">
                    <Icon sx={{width: '40%', height: '40%', marginLeft: '1vh', marginRight: '1vh'}}>
                        <img src={Logo}/>
                    </Icon>
                <Container maxWidth="sm">
                    <Grid container direction="column" alignItems="center" spacing={4}>
                        <Grid item>
                            <Typography variant="h4" component="h1">
                                {t('landing_page.title')}
                            </Typography>
                        </Grid>
                        <Grid item>
                            <Typography variant="subtitle1">
                                {t('landing_page.subtitle')}
                            </Typography>
                        </Grid>
                        {!loggedIn &&
                            <Grid item>
                                <Button className="landing-page-button" variant="contained" color="primary"
                                        onClick={() => handleButtonClick('/register')}>
                                    {t('navbar.register')}
                                </Button>
                                <Button className="landing-page-button" variant="contained" color="primary"
                                        onClick={() => handleButtonClick('/login')}>
                                    {t('navbar.login')}
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