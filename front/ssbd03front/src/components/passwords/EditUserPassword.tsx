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
import {useParams} from "react-router-dom";
import {useEffect} from "react";

const EditPassword = () => {
    const [cookies] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;
    const username = useParams().username;
    const [etag, setEtag] = React.useState(false);
    const [version, setVersion] = React.useState("");

    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const [newPassword, setNewPassword] = React.useState("");
    const [repeatedNewPassword, setRepeatedNewPassword] = React.useState("");

    const [newPasswordError, setNewPasswordError] = React.useState("");
    const [repeatedNewPasswordError, setRepeatedNewPasswordError] = React.useState("");
    const [newAndRepeatedNewPasswordNotSameError, setNewAndRepeatedNewPasswordNotSameError] = React.useState("");
    const [dataError, setDataError] = React.useState("");

    const [validData, setValidData] = React.useState(false);

    const [successOpen, setSuccessOpen] = React.useState(false);
    const [errorOpen, setErrorOpen] = React.useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = React.useState("");

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    useEffect(() => {
        const fetchData = async () => {
            await axios.get(`${API_URL}/accounts/${username}`, {
                headers: {
                    Authorization: 'Bearer ' + token
                }
            })
                .then(response => {
                    setEtag(response.headers["etag"]);
                    setVersion(response.data.version)
                });
        };
        fetchData();
    }, []);

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
        const passwordDTO = {
            newPassword: newPassword.toString(),
            repeatedNewPassword: repeatedNewPassword.toString(),
            version: parseInt(version)
        }

        axios.patch(`${API_URL}/accounts/${username}/password`,
            passwordDTO, {
                headers: {
                    'Authorization': token,
                    'If-Match' : etag,
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

    const handleConfirm = () => {
        if(validData) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError("Wprowadź poprawne dane");
        }
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
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
                        <form onSubmit={handleSubmit}>
                            <List component="nav" aria-label="mailbox folders">
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
                <DialogTitle>Czy na pewno chcesz zmienić hasło?</DialogTitle>
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
export default EditPassword;