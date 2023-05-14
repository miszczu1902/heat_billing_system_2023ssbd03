import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { TextField } from '@mui/material';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import axios from 'axios';
import { API_URL } from '../../consts';
import {useCookies} from 'react-cookie';

export default function EditPassword() {
    const [cookies] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;
    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const [oldPassword, setOldPassword] = React.useState("");
    const [newPassword, setNewPassword] = React.useState("");
    const [repeatedNewPassword, setRepeatedNewPassword] = React.useState("");

    const [oldPasswordError, setOldPasswordError] = React.useState("");
    const [newPasswordError, setNewPasswordError] = React.useState("");
    const [repeatedNewPasswordError, setRepeatedNewPasswordError] = React.useState("");
    const [oldAndNewPasswordSameError, setOldAndNewPasswordSameError] = React.useState("");
    const [newAndRepeatedNewPasswordNotSameError, setNewAndRepeatedNewPasswordNotSameError] = React.useState("");
    const [dataError, setDataError] = React.useState("");

    const [newPasswordValid, setNewPasswordValid] = React.useState(false);
    const [repeatedNewPasswordValid, setRepeatedNewPasswordValid] = React.useState(false);
    const [oldPasswordValid, setOldPasswordValid] = React.useState(false);
    const [oldAndNewPasswordSameValid, setOldAndNewPasswordSameValid] = React.useState(false);
    const [newAndRepeatedNewPasswordNotSameValid, setNewAndRepeatedNewPasswordNotSameValid] = React.useState(false);
    const [validData, setValidData] = React.useState(false);

    const [successOpen, setSuccessOpen] = React.useState(false);
    const [errorOpen, setErrorOpen] = React.useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = React.useState("");

    const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const checkOldAndNewPasswords = (oldPassword: string, newPassword: string): boolean => {
        if (oldPassword === newPassword) {
            setOldAndNewPasswordSameError("Stare i nowe hasło nie mogą być takie same");
            setOldAndNewPasswordSameValid(false);
            return false;
        }
        setOldAndNewPasswordSameError("");
        setOldAndNewPasswordSameValid(true);
        return true;
    }

    const checkNewAndRepeatedNewPasswords = (newPassword: string, repeatedNewPassword: string): boolean => {
        if (newPassword !== repeatedNewPassword) {
            setNewAndRepeatedNewPasswordNotSameError("Nowe i powtórzone nowe hasło muszą być takie same");
            setNewAndRepeatedNewPasswordNotSameValid(false);
            return false;
        }
        setNewAndRepeatedNewPasswordNotSameError("");
        setNewAndRepeatedNewPasswordNotSameValid(true);
        return true;
    }

    const handleOldPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let password = event.target.value;
        setOldPassword(password)
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
        let oldAndNewPasswordSameValidNow = checkOldAndNewPasswords(password, newPassword);

        if (!regex.test(password)) {
            setOldPasswordError("Hasło musi zawierać conajmniej 8 znaków, jedną wielką i małą literę, " +
                "cyfrę i jeden ze znaków specjalnych: @$!%*?&");
            setValidData(false);
        } else {
            setOldPasswordError("");
            setOldPasswordValid(true);
            if (newPasswordValid && repeatedNewPasswordValid && oldAndNewPasswordSameValidNow &&
                newAndRepeatedNewPasswordNotSameValid) {
                setValidData(true);
            }
        }
    };

    const handleNewPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let password = event.target.value;
        setNewPassword(password);
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
        let oldAndNewPasswordSameValidNow = checkOldAndNewPasswords(oldPassword, password);
        let newAndRepeatedNewPasswordNotSameValidNow = checkNewAndRepeatedNewPasswords(password, repeatedNewPassword);
        if (!regex.test(password)) {
            setNewPasswordError("Hasło musi zawierać conajmniej 8 znaków, jedną wielką i małą literę, " +
                "cyfrę i jeden ze znaków specjalnych: @$!%*?&");
            setValidData(false);
        } else {
            setNewPasswordError("");
            setNewPasswordValid(true);
            if (oldPasswordValid && repeatedNewPasswordValid && oldAndNewPasswordSameValidNow &&
                newAndRepeatedNewPasswordNotSameValidNow) {
                setValidData(true);
            }
        }
    };

    const handleRepeatedNewPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let password = event.target.value;
        setRepeatedNewPassword(password);
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
        let newAndRepeatedNewPasswordNotSameValidNow = checkNewAndRepeatedNewPasswords(newPassword, password);
        if (!regex.test(password)) {
            setRepeatedNewPasswordError("Hasło musi zawierać conajmniej 8 znaków, jedną wielką i małą literę, " +
                "cyfrę i jeden ze znaków specjalnych: @$!%*?&");
            setRepeatedNewPasswordValid(false);
            setValidData(false);
        } else {
            setRepeatedNewPasswordError("");
            setRepeatedNewPasswordValid(true);
            if (oldPasswordValid && newPasswordValid && oldAndNewPasswordSameValid &&
                newAndRepeatedNewPasswordNotSameValidNow) {
                setValidData(true);
            }
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

    const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
    }

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
        const personalDataDTO = {
            oldPassword: oldPassword.toString(),
            newPassword: newPassword.toString(),
            repeatedNewPassword: repeatedNewPassword.toString()
        }

         axios.patch(`${API_URL}/accounts/self/password`,
                personalDataDTO, {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    },
                })
                .then(response => {
                    setSuccessOpen(true);
                })
                .catch(error => {
                    setErrorOpenMessage(error.response.data.message)
                    setErrorOpen(true);
                });
        handleClose(event, reason);
    }

    const handleConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if(validData) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError("Wprowadź poprawne dane");
        }
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setOldPassword("");
        setNewPassword("");
        setRepeatedNewPassword("");
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
        <div>
            <div>
                <Button onClick={handleClickOpen} variant="contained">Zmień hasło</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                <DialogTitle>Wypełnij formularz zmiany hasła</DialogTitle>
                <DialogContent>
                    <Box sx={{ display: 'flex', flexWrap: 'wrap' }}>
                        <form onSubmit={handleSumbit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handleOldPasswordChange}>
                                        <TextField
                                            id="outlined-helperText"
                                            label="Stare hasło"
                                            defaultValue= {oldPassword}
                                            type="password"
                                            helperText="Wprowadź stare hasło"
                                        />
                                        <div className="form-group">
                                            {oldPasswordError}
                                        </div>
                                    </div>
                                </ListItem>
                                <ListItem>
                                    <div className="form-group" onChange={handleNewPasswordChange}>
                                        <TextField
                                            id="outlined-helperText"
                                            label="Nowe hasło"
                                            defaultValue= {newPassword}
                                            type="password"
                                            helperText="Wprowadź nowe hasło"
                                        />
                                        <div className="form-group">
                                            {newPasswordError}
                                        </div>
                                    </div>
                                </ListItem>
                                <ListItem>
                                    <div className="form-group" onChange={handleRepeatedNewPasswordChange}>
                                        <TextField
                                            id="outlined-helperText"
                                            label="Powtórz nowe hasło"
                                            defaultValue= {repeatedNewPassword}
                                            type="password"
                                            helperText="Powtórz nowe hasło"
                                        />
                                        <div className="form-group">
                                            {repeatedNewPasswordError}
                                        </div>
                                    </div>
                                </ListItem>
                            </List>
                            <div className="form-group">
                                {oldAndNewPasswordSameError}
                            </div>
                            <div className="form-group">
                                {newAndRepeatedNewPasswordNotSameError}
                            </div>
                            <div className="form-group">
                                {dataError}
                            </div>
                        </form>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleConfirm} disabled={!validData}>Ok</Button>
                </DialogActions>
            </Dialog>

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
        </div>
    );
}