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
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogActions from "@mui/material/DialogActions";
import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {Icon} from "@mui/material";
import Logo from "../../assets/logo.svg";

const ResetPassword = () => {
    const [newPassword, setNewPassword] = React.useState("");
    const [repeatedNewPassword, setRepeatedNewPassword] = React.useState("");
    const theme = createTheme();

    const [newPasswordError, setNewPasswordError] = React.useState("");
    const [repeatedNewPasswordError, setRepeatedNewPasswordError] = React.useState("");
    const [newAndRepeatedNewPasswordNotSameError, setNewAndRepeatedNewPasswordNotSameError] = React.useState("");

    const [successOpen, setSuccessOpen] = React.useState(false);
    const [errorOpen, setErrorOpen] = React.useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = React.useState("");
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const [validData, setValidData] = React.useState(true);
    const [open, setOpen] = React.useState(false);
    const navigate = useNavigate();
    const searchParams = new URLSearchParams(window.location.search);
    const token = searchParams.get('token');
    const [showMessage, setShowMessage] = useState<boolean>(false);

    useEffect(() => {
        if (token === null) {
            setShowMessage(true);
            setTimeout(() => {
                navigate('/');
            }, 6000);
        }

    }, [token, navigate]);

    if (showMessage) {
        return <div>Brak ważnego tokenu do resetu hasła, trwa przekierowanie...</div>;
    }
    const handleNewPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let password = event.target.value;
        setNewPassword(password);
        let newAndRepeatedNewPasswordSame = checkNewAndRepeatedNewPasswords(password, repeatedNewPassword)

        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
        if (!regex.test(password)) {
            setNewPasswordError("Hasło musi zawierać conajmniej 8 znaków, jedną wielką i małą literę, " +
                "cyfrę i jeden ze znaków specjalnych: @$!%*?&");
            setValidData(false);
        } else {
            setNewPasswordError("");
            if (newAndRepeatedNewPasswordSame) {
                setValidData(true);
            } else {
                setValidData(false);
            }
        }
    };

    const handleRepeatedNewPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let password = event.target.value;
        setRepeatedNewPassword(password);
        let newAndRepeatedNewPasswordSame = checkNewAndRepeatedNewPasswords(newPassword, password)

        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
        if (!regex.test(password)) {
            setRepeatedNewPasswordError("Hasło musi zawierać conajmniej 8 znaków, jedną wielką i małą literę, " +
                "cyfrę i jeden ze znaków specjalnych: @$!%*?&");
            setValidData(false);
        } else {
            setRepeatedNewPasswordError("");
            if (newAndRepeatedNewPasswordSame) {
                setValidData(true);
            } else {
                setValidData(false);
            }
        }
    };

    const checkNewAndRepeatedNewPasswords = (newPassword: string, repeatedNewPassword: string): boolean => {
        if (newPassword !== repeatedNewPassword) {
            setNewAndRepeatedNewPasswordNotSameError("Nowe i powtórzone nowe hasło muszą być takie same");
            return false
        } else {
            setNewAndRepeatedNewPasswordNotSameError("");
            return true
        }
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
    }

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
    }

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {

        if (token !== null) {
            const resetPasswordFromEmailDTO = {
                resetPasswordToken: token.toString(),
                newPassword: newPassword.toString(),
                repeatedNewPassword: repeatedNewPassword.toString()
            }

            axios.patch(`${API_URL}/accounts/reset-password-from-email`,
                resetPasswordFromEmailDTO, {
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

    const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setOpen(false);
        }
    };

    const handleConfirm = () => {
        if (validData) {
            setConfirmOpen(true);
        }
    }

    const handleSuccessClose = () => {
        navigate('/login');
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container justifyContent="center" alignItems="center" sx={{background: '#1c8de4', height: '100vh', width: '100vw'}}>
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Icon sx={{width: '10%', height: '10%', marginLeft: '1vh'}}>
                            <img src={Logo}/>
                        </Icon>
                        <Typography variant="h5"> Zmiana hasła </Typography>
                        <Box component="form" onSubmit={handleSubmit}>
                            <Box component="form">
                                <TextField fullWidth margin="normal" label="Nowe hasło" type="password"
                                           value={newPassword}
                                           helperText="Wprowadź nowe hasło" onChange={handleNewPasswordChange}/>
                                <div className="form-group" style={{textAlign: "center"}}>
                                    {newPasswordError}
                                </div>
                                <TextField fullWidth margin="normal" label="Powtórzone nowe hasło" type="password"
                                           helperText="Powtórz nowe hasło" onChange={handleRepeatedNewPasswordChange}
                                           value={repeatedNewPassword}/>
                                <div className="form-group">
                                    {repeatedNewPasswordError}
                                </div>
                            </Box>
                            <div className="form-group">
                                {newAndRepeatedNewPasswordNotSameError}
                            </div>
                            <Button onClick={handleConfirm} fullWidth variant="contained">Zmień hasło</Button>
                            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                                <DialogTitle>Czy na pewno chcesz zmienić swoje hasło?</DialogTitle>
                                <DialogActions>
                                    <Button onClick={handleConfirmClose}>Nie</Button>
                                    <Button onClick={handleConfirmConfirm}>Tak</Button>
                                </DialogActions>
                            </Dialog>
                            <Dialog disableEscapeKeyDown open={successOpen}>
                                <DialogTitle>Hasło zostało zmienione</DialogTitle>
                                <Button onClick={handleSuccessClose}>Ok</Button>
                            </Dialog>
                            <Dialog disableEscapeKeyDown open={errorOpen}>
                                <DialogTitle>{errorOpenMessage}</DialogTitle>
                                <Button onClick={handleErrorClose}>Ok</Button>
                            </Dialog>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    );
}
export default ResetPassword;

