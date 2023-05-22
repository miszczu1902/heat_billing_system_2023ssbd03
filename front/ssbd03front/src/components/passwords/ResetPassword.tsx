import * as React from 'react';
import {useEffect, useState} from 'react';
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
import {useNavigate} from 'react-router-dom';
import {Icon} from "@mui/material";
import Logo from "../../assets/logo.svg";
import {useTranslation} from "react-i18next";

const ResetPassword = () => {
    const {t, i18n} = useTranslation();
    const [newPassword, setNewPassword] = useState("");
    const [repeatedNewPassword, setRepeatedNewPassword] = useState("");
    const theme = createTheme();

    const [newPasswordError, setNewPasswordError] = useState("");
    const [repeatedNewPasswordError, setRepeatedNewPasswordError] = useState("");
    const [newAndRepeatedNewPasswordNotSameError, setNewAndRepeatedNewPasswordNotSameError] = useState("");

    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = useState("");
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [validData, setValidData] = useState(true);
    const [open, setOpen] = useState(false);
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
        return <div>{t('reset_password.error_token')}</div>;
    }
    const handleNewPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let password = event.target.value;
        setNewPassword(password);
        let newAndRepeatedNewPasswordSame = checkNewAndRepeatedNewPasswords(password, repeatedNewPassword)

        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
        if (!regex.test(password)) {
            setNewPasswordError(t('edit_password.old_password_error_one') +
                t('edit_password.old_password_error_two'));
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
            setRepeatedNewPasswordError(t('edit_password.old_password_error_one') +
                t('edit_password.old_password_error_two'));
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
            setNewAndRepeatedNewPasswordNotSameError(t('edit_password.new_and_repeated_new_password_not_same_error'));
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

    const backToLogin = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        navigate('/login');
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
            <Grid container justifyContent="center" alignItems="center"
                  sx={{background: '#1c8de4', height: '100vh', width: '100vw'}}>
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <Icon sx={{width: '10%', height: '10%', marginLeft: '1vh'}}>
                            <img src={Logo}/>
                        </Icon>
                        <Typography variant="h5">{t('reset_password.password_change')}</Typography>
                        <Box component="form" onSubmit={handleSubmit}>
                            <Box component="form">
                                <TextField fullWidth margin="normal" label={t('edit_password.label_text_new_password')}
                                           type="password"
                                           value={newPassword}
                                           helperText={t('edit_password.help_text_new_password')}
                                           onChange={handleNewPasswordChange}/>
                                <div className="form-group" style={{textAlign: "center"}}>
                                    {newPasswordError}
                                </div>
                                <TextField fullWidth margin="normal"
                                           label={t('edit_password.label_text_repeated_password')} type="password"
                                           helperText={t('edit_password.help_text_repeated_password')}
                                           onChange={handleRepeatedNewPasswordChange}
                                           value={repeatedNewPassword}/>
                                <div className="form-group">
                                    {repeatedNewPasswordError}
                                </div>
                            </Box>
                            <div className="form-group">
                                {newAndRepeatedNewPasswordNotSameError}
                            </div>
                            <Button onClick={handleConfirm} fullWidth variant="contained"
                                    style={{marginBottom: '5px'}}>{t('reset_password.change_password')}</Button>
                            <Button onClick={backToLogin} fullWidth
                                    variant="contained">{t('reset_password.back_to_login')}</Button>
                            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                                <DialogTitle>{t('edit_password.confirm_title')}</DialogTitle>
                                <DialogActions>
                                    <div>
                                        <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                                    </div>
                                    <div>
                                        <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                                    </div>
                                </DialogActions>
                            </Dialog>
                            <Dialog disableEscapeKeyDown open={successOpen}>
                                <DialogTitle>{t('edit_password.success_title')}</DialogTitle>
                                <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
                            </Dialog>
                            <Dialog disableEscapeKeyDown open={errorOpen}>
                                <DialogTitle>{errorOpenMessage}</DialogTitle>
                                <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
                            </Dialog>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    );
}
export default ResetPassword;

