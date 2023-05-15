import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import {API_URL} from '../../consts';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import axios from 'axios';
import {useCookies} from 'react-cookie';
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import DialogActions from "@mui/material/DialogActions";
import {useNavigate} from "react-router-dom";

const theme = createTheme();

const Login = () => {
    const navigate = useNavigate();
    const [cookies, setCookie] = useCookies(["token"]);
    const [username, setUsername] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [loginError, setLoginError] = React.useState("");
    const [open, setOpen] = React.useState(false);
    const [loginPassword, setLoginPassword] = React.useState("");

    const [loginPasswordError, setLoginPasswordError] = React.useState("");
    const [validData, setValidData] = React.useState(false);
    const [successOpen, setSuccessOpen] = React.useState(false);
    const [errorOpen, setErrorOpen] = React.useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = React.useState("");
    const [loading, setLoading] = React.useState(true);
    const [loggedIn, setLoggedIn] = React.useState(false);

    React.useEffect(() => {
        if (cookies.token) {
            setLoggedIn(true);
        }
        setLoading(false);
    }, [cookies]);

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
            setLoginError('Login musi składać się z 6-16 znaków i składać się z liter i cyfr');
        } else if (!regexPassword.test(password)) {
            setPassword("");
            setLoginError('Hasło musi składać się z 8-32 znaków, jednej dużej litery, znak specjalny');
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
                    setCookie("token", response.headers["bearer"])
                    navigate('/');
                })
                .catch((error) => {
                    setPassword("");
                    setLoginError(error.response.data.message);
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
            setLoginPasswordError("Login może zawierać tylko litery, cyfry, znak podkreślenia oraz " +
                "musi mieć długość od 8 do 16 znaków.");
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
        if(validData) {
            const resetPasswordDTO = {
                username: loginPassword.toString(),
            }

            axios.post(`${API_URL}/accounts/reset-password`,
                resetPasswordDTO, {
                    headers: {
                        'Content-Type': 'application/json'
                    },
                })
                .then(() => {
                    setSuccessOpen(true);
                })
                .catch(error => {
                    setErrorOpenMessage(error.response.data.message)
                    setErrorOpen(true);
                });
            handleClose(event, reason);
        }
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container justifyContent="center" alignItems="center">
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Typography variant="h5"> Logowanie </Typography>
                        <Typography sx={{color: 'red'}}>{loginError}</Typography>
                        <Box component="form" onSubmit={handleSubmit}>
                            <TextField fullWidth margin="normal" label="Login" value={username}
                                       helperText="Wprowadź Login" onChange={handleUsernameChange}/>
                            <TextField fullWidth margin="normal" label="Hasło" type="password"
                                       helperText="Wprowadź hasło" onChange={handlePasswordChange}
                                       value={password}/>
                            <Button type="submit" fullWidth variant="contained">Zaloguj</Button>
                            <Box sx={{my: 1, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                <div>
                                    <div>
                                        <Button onClick={handleClickOpen} variant="contained">Zapomniałem hasła</Button>
                                    </div>
                                    <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                                        <DialogTitle>Przypomnienie hasła</DialogTitle>
                                        <DialogContent>
                                            <Box sx={{ display: 'flex', flexWrap: 'wrap' }}>
                                                <form onSubmit={handleSubmitPasswordChange}>
                                                    <List component="nav" aria-label="mailbox folders">
                                                        <ListItem>
                                                            <div className="form-group" onChange={handleLoginPasswordChange}>
                                                                <TextField
                                                                    id="outlined-helperText"
                                                                    label="Login"
                                                                    helperText="Wprowadź Login"
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
                                            <Button onClick={handleClose}>Cancel</Button>
                                            <Button onClick={handleConfirm} disabled={!validData}>Ok</Button>
                                        </DialogActions>
                                    </Dialog>
                                    <Dialog disableEscapeKeyDown open={successOpen}>
                                        <DialogTitle>Na adres email przypisany do tego konta została wysłana wiadomość z
                                            hiperłączem do formularza zmiany hasła.</DialogTitle>
                                        <Button onClick={handleSuccessClose}>Ok</Button>
                                    </Dialog>

                                    <Dialog disableEscapeKeyDown open={errorOpen}>
                                        <DialogTitle>{errorOpenMessage}</DialogTitle>
                                        <Button onClick={handleErrorClose}>Ok</Button>
                                    </Dialog>
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