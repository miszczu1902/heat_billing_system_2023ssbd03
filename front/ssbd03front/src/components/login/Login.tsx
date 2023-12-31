import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import {API_URL} from '../../consts';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import axios from 'axios';
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import DialogActions from "@mui/material/DialogActions";
import {useNavigate} from "react-router-dom";
import {Icon, Snackbar, Alert} from "@mui/material";
import Logo from './../../assets/logo.svg';
import {useTranslation} from "react-i18next";

const theme = createTheme();

const Login = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loginError, setLoginError] = useState("");
    const [open, setOpen] = useState(false);
    const [loginPassword, setLoginPassword] = useState("");

    const [loginPasswordError, setLoginPasswordError] = useState("");
    const [validData, setValidData] = useState(false);
    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = useState("");
    const [loading, setLoading] = useState(true);
    const [loggedIn, setLoggedIn] = useState(false);

    React.useEffect(() => {
        if (localStorage.getItem("token") != null) {
            setLoggedIn(true);
        }
        setLoading(false);
    }, [localStorage.getItem("token")]);

    if (loading) {
        return <p></p>;
    }

    if (loggedIn) {
        navigate("/");
        return null;
    }

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
    };

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const regexLogin = /^[a-zA-Z0-9_]{6,16}$/;
        const regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
        if (!regexLogin.test(username)) {
            setLoginError(t('register.register_error_login'));
        } else if (!regexPassword.test(password)) {
            setPassword("");
            setLoginError(t('register.register_error_password'));
        } else {
            let data = JSON.stringify({
                "username": username,
                "password": password
            });

            let config = {
                method: 'post',
                maxBodyLength: Infinity,
                url: API_URL + '/accounts/login',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: data,
            };
            axios.request(config)
                .then((response) => {
                    localStorage.setItem("token", response.headers["bearer"]);
                    i18n.changeLanguage(response.headers["language"].toLowerCase());
                    navigate('/');
                })
                .catch((error) => {
                    setPassword("");
                    setLoginError(t("exception.account.invalid_credentials"));
                });
        }
    };

    const handleSubmitPasswordChange = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleLoginPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let loginPassword = event.target.value;
        setLoginPassword(loginPassword)
        const regex = /^[a-zA-Z0-9_]{6,16}$/;
        if (!regex.test(loginPassword)) {
            setLoginPasswordError(t('login.login_password_error_one') +
                t('login.login_password_error_two'));
            setValidData(false);
        } else {
            setLoginPasswordError("");
            setValidData(true);
        }
    };

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    };

    const handleConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (validData && loginPassword.toString().length !== 0) {
            const resetPasswordDTO = {
                username: loginPassword.toString(),
            }

            axios.post(`${API_URL}/accounts/reset-password`,
                resetPasswordDTO, {
                    headers: {
                        'Content-Type': 'application/json'
                    },
                })
                .then((response) => {
                    setSuccessOpen(true);

                })
                .catch(error => {
                        setErrorOpenMessage(t(error.response.data.message))
                        setErrorOpen(true);
                    });
            handleClose(event, reason);
        }
    }

    const handleSuccessClose = () => {
        setSuccessOpen(false);
    }

    const handleErrorClose = () => {
        setErrorOpen(false);
    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container justifyContent="center" alignItems="center"
                  sx={{background: '#1c8de4', height: '100vh', width: '100vw'}}>
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box sx={{my: 20, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Icon onClick={() => navigate('/')}
                              sx={{width: '10%', height: '10%', marginLeft: '1vh', cursor: 'pointer'}}>
                            <img src={Logo}/>
                        </Icon>
                        <Typography variant="h5">{t('login.title')}</Typography>
                        <Typography sx={{color: 'red'}}>{loginError}</Typography>
                        <Box component="form" onSubmit={handleSubmit}>
                            <TextField fullWidth margin="normal" label={t('login.username')} value={username}
                                       helperText={t('login.enter_username')} onChange={handleUsernameChange}/>
                            <TextField fullWidth margin="normal" label={t('login.password')} type="password"
                                       helperText={t('login.enter_password')} onChange={handlePasswordChange}
                                       value={password}/>
                            <Box sx={{
                                display: 'flex',
                                flexWrap: 'wrap',
                                alignItems: 'center',
                                justifyContent: 'center'
                            }}>
                                <Button type="submit" variant="contained" sx={{m: 2}}>{t('login.login')}</Button>
                                <Button onClick={handleClickOpen} variant="contained"
                                        sx={{m: 2}}>{t('login.password_forgot')}</Button>
                                <div>
                                    <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                                        <DialogTitle>{t('login.password_reminder')}</DialogTitle>
                                        <DialogContent>
                                            <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                                                <form onSubmit={handleSubmitPasswordChange}>
                                                    <List component="nav" aria-label="mailbox folders">
                                                        <ListItem>
                                                            <div className="form-group"
                                                                 onChange={handleLoginPasswordChange}>
                                                                <TextField
                                                                    id="outlined-helperText"
                                                                    label={t('login.username')}
                                                                    helperText={t('login.enter_username')}
                                                                />
                                                                <div className="form-group">
                                                                    {loginPasswordError}
                                                                </div>
                                                            </div>
                                                        </ListItem>
                                                    </List>
                                                </form>
                                            </Box>
                                        </DialogContent>
                                        <DialogActions>
                                            <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                                            <Button onClick={handleConfirm}
                                                    disabled={!validData}>{t('confirm.ok')}</Button>
                                        </DialogActions>
                                    </Dialog>
                                
                                    <Snackbar open={successOpen} autoHideDuration={6000} onClose={handleSuccessClose}>
                                        <Alert onClose={handleSuccessClose} severity="success" sx={{width: '100%'}}>
                                            {t('login.success_title')}
                                        </Alert>
                                    </Snackbar>
                                    <Snackbar open={errorOpen} autoHideDuration={6000} onClose={handleErrorClose}>
                                        <Alert onClose={handleErrorClose} severity="error" sx={{width: '100%'}}>
                                            {t(errorOpenMessage)}
                                        </Alert>
                                    </Snackbar>
                                </div>
                            </Box>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    );
}

export default Login;