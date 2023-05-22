import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {TextField} from '@mui/material';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useCookies} from 'react-cookie';
import {useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import { useState } from 'react';

const EditPassword = () => {
    const {t, i18n} = useTranslation();
    const [cookies] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;
    const username = useParams().username;
    const [etag, setEtag] = useState(false);
    const [version, setVersion] = useState("");

    const [open, setOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [newPassword, setNewPassword] = useState("");
    const [repeatedNewPassword, setRepeatedNewPassword] = useState("");

    const [newPasswordError, setNewPasswordError] = useState("");
    const [repeatedNewPasswordError, setRepeatedNewPasswordError] = useState("");
    const [newAndRepeatedNewPasswordNotSameError, setNewAndRepeatedNewPasswordNotSameError] = useState("");
    const [dataError, setDataError] = useState("");

    const [validData, setValidData] = useState(false);

    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorOpenMessage, setErrorOpenMessage] = useState("");

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
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

    const handleClickOpen = () => {
        const fetchData = async () => {
            await axios.get(`${API_URL}/accounts/${username}`, {
                headers: {
                    Authorization: token
                }
            })
                .then(response => {
                    setEtag(response.headers["etag"]);
                    setVersion(response.data.version)
                });
        };
        fetchData();
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
                    'If-Match': etag,
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
        if (validData) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError('edit_password.data_error');
        }
    }

    const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        setNewPassword("");
        setRepeatedNewPassword("");
        if (reason !== 'backdropClick') {
            setSuccessOpen(false);
        }
        window.location.reload();
    }

    const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setErrorOpen(false);
        }
    };

    return (
        <div>
            <div>
                <Button onClick={handleClickOpen} variant="contained">{t('edit_password.button_title')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                <DialogTitle>{t('edit_password.form_title')}</DialogTitle>
                <DialogContent>
                    <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                        <form onSubmit={handleSubmit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handleNewPasswordChange}>
                                        <TextField
                                            id="outlined-helperText"
                                            label={t('edit_password.label_text_new_password')}
                                            defaultValue={newPassword}
                                            type="password"
                                            helperText={t('edit_password.help_text_new_password')}
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
                                            label={t('edit_password.label_text_repeated_password')}
                                            defaultValue={repeatedNewPassword}
                                            type="password"
                                            helperText={t('edit_password.help_text_repeated_password')}
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
                    <Button onClick={handleClose}>{t("confirm.cancel")}</Button>
                    <Button onClick={handleConfirm} disabled={!validData}>{t("confirm.ok")}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                <DialogTitle>{t("edit_password.confirm_title")}</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t("confirm.no")}</Button>
                    <Button onClick={handleConfirmConfirm}>{t("confirm.yes")}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={successOpen}>
                <DialogTitle>{t("edit_password.success_title")}</DialogTitle>
                <Button onClick={handleSuccessClose}>{t("confirm.ok")}</Button>
            </Dialog>

            <Dialog disableEscapeKeyDown open={errorOpen}>
                <DialogTitle>{errorOpenMessage}</DialogTitle>
                <Button onClick={handleErrorClose}>{t("confirm.ok")}</Button>
            </Dialog>
        </div>
    );
}
export default EditPassword;