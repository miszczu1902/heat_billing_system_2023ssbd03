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
import {useTranslation} from "react-i18next";

const EditPassword = () => {
    const {t, i18n} = useTranslation();
    const [cookies] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;
    const [etag, setEtag] = React.useState(false);
    const [version, setVersion] = React.useState("");

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
            setOldAndNewPasswordSameError(t('edit_password.old_and_new_password_same_error'));
            setOldAndNewPasswordSameValid(false);
            return false;
        }
        setOldAndNewPasswordSameError("");
        setOldAndNewPasswordSameValid(true);
        return true;
    }

    const checkNewAndRepeatedNewPasswords = (newPassword: string, repeatedNewPassword: string): boolean => {
        if (newPassword !== repeatedNewPassword) {
            setNewAndRepeatedNewPasswordNotSameError(t('edit_password.new_and_repeated_new_password_not_same_error'));
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
            setOldPasswordError(t('edit_password.old_password_error_one') +
                t('edit_password.old_password_error_two'));
            setValidData(false);
        } else {
            setOldPasswordError("");
            setOldPasswordValid(true);
            if (newPasswordValid && repeatedNewPasswordValid && oldAndNewPasswordSameValidNow &&
                newAndRepeatedNewPasswordNotSameValid) {
                setValidData(true);
            } else {
                setValidData(false);
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
            setOldPasswordError(t('edit_password.old_password_error_one') +
                t('edit_password.old_password_error_two'));
            setValidData(false);
        } else {
            setNewPasswordError("");
            setNewPasswordValid(true);
            if (oldPasswordValid && repeatedNewPasswordValid && oldAndNewPasswordSameValidNow && newAndRepeatedNewPasswordNotSameValidNow) {
                setValidData(true);
            } else {
                setValidData(false);
            }
        }
    };

    const handleRepeatedNewPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let password = event.target.value;
        setRepeatedNewPassword(password);
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/;
        let newAndRepeatedNewPasswordNotSameValidNow = checkNewAndRepeatedNewPasswords(newPassword, password);
        if (!regex.test(password)) {
            setRepeatedNewPasswordError(t('edit_password.old_password_error_one') +
                t('edit_password.old_password_error_two'));
            setRepeatedNewPasswordValid(false);
            setValidData(false);
        } else {
            setRepeatedNewPasswordError("");
            setRepeatedNewPasswordValid(true);
            if (oldPasswordValid && newPasswordValid && oldAndNewPasswordSameValid && newAndRepeatedNewPasswordNotSameValidNow) {
                setValidData(true);
            } else {
                setValidData(false);
            }
        }
    };

    const handleClickOpen = () => {
        const fetchData = async () => {
            await axios.get(`${API_URL}/accounts/self`, {
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
            oldPassword: oldPassword.toString(),
            newPassword: newPassword.toString(),
            repeatedNewPassword: repeatedNewPassword.toString(),
            version: parseInt(version)
        }

         axios.patch(`${API_URL}/accounts/self/password`,
             passwordDTO, {
                    headers: {
                        'Authorization': token,
                        'If-Match' : etag,
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

    const handleConfirm = () => {
        if(validData) {
            setDataError("");
            setConfirmOpen(true);
        } else {
            setDataError(t('edit_password.data_error'));
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
                <Button onClick={handleClickOpen} variant="contained">{t('edit_password.button_title')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
                <DialogTitle>{t('edit_password.form_title')}</DialogTitle>
                <DialogContent>
                    <Box sx={{ display: 'flex', flexWrap: 'wrap' }}>
                        <form onSubmit={handleSumbit}>
                            <List component="nav" aria-label="mailbox folders">
                                <ListItem>
                                    <div className="form-group" onChange={handleOldPasswordChange}>
                                        <TextField
                                            id="outlined-helperText"
                                            label={t('edit_password.label_text_old_password')}
                                            defaultValue= {oldPassword}
                                            type="password"
                                            helperText={t('edit_password.help_text_old_password')}
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
                                            label={t('edit_password.label_text_new_password')}
                                            defaultValue= {newPassword}
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
                                            defaultValue= {repeatedNewPassword}
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
                    <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                    <Button onClick={handleConfirm} disabled={!validData}>{t("confirm.ok")}</Button>
                </DialogActions>
            </Dialog>

            <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
                <DialogTitle>{t('edit_password.confirm_title')}</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
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
        </div>
    );
}
export default EditPassword;