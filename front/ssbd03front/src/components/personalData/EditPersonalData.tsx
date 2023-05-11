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
import validator from "validator";
import { API_URL } from '../../consts';
import { useState, useEffect } from 'react';
import e from 'express';

const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaXN6Y3p1MjEzNyIsImlhdCI6MTY4MzU2NTAxNywicm9sZSI6Ik9XTkVSIiwiZXhwIjoxNjgzNTY2ODE3fQ.ddfkn394c0xhJsY2d58P5uQGJD7Lu4iY3Of7stt9NgI';

export default function EditPersonalData() {
  const [open, setOpen] = React.useState(false);
  const [confirmOpen, setConfirmOpen] = React.useState(false);

  var [name, setName] = React.useState("");
  var [surname, setSurname] = React.useState("");
  var [newName, setNewName] = React.useState("");
  var [newSurname, setNewSurname] = React.useState("");

  var [nameError, setNameError] = React.useState("");
  var [surnameError, setSurnameError] = React.useState("");
  var [dataError, setDataError] = React.useState("");

  var [validData, setValidData] = React.useState(false);

  var [successOpen, setSuccessOpen] = React.useState(false);
  var [errorOpen, setErrorOpen] = React.useState(false);

  useEffect(() => {
    axios.get(`${API_URL}/accounts/self/personal-data`, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    })
    .then(response => {
      setName(response.data.firstName.toString());
      setSurname(response.data.surname.toString());
    })
    .catch(error => {
      // handle error
    });
  }, []);

  const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
}
  const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (validator.isAlpha(event.target.value) && event.target.value.length <= 32 && event.target.value.length > 0) {
      setNameError("");
      newName = event.target.value;
      setValidData(true);
    } else {
      setNameError("Imię może zawierać tylko litery i musi mieć długość do 32 znaków");
      setValidData(false);
    }
  };

  const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => { 
    if (validator.isAlpha(event.target.value) && event.target.value.length <= 32 && event.target.value.length > 0) {
      setSurnameError("");
      newSurname = event.target.value;
      setValidData(true);
    } else {
      setSurnameError("Nazwisko może zawierać tylko litery i musi mieć długość do 32 znaków");
      setValidData(false); 
    } 
  };

  const handleClickOpen = () => {
    axios.get(`${API_URL}/accounts/self/personal-data`, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    })
    .then(response => {
      setName(response.data.firstName.toString());
      setSurname(response.data.surname.toString());
    })
    .catch(error => {
      // handle error
    });
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
    setName(newName);
    setSurname(newSurname);
    if(nameError === "" && surnameError === "") {
      const data = { firstName: name, surname: surname};
      axios.patch(`${API_URL}/accounts/self/personal-data`, {
        headers: {
          Authorization: 'Bearer ' + token
        }, 
        data: data
      })
      .then(response => {
        setSuccessOpen(true);
      })
      .catch(error => {
        setErrorOpen(true);
      });
    }
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
    if (reason !== 'backdropClick') {
      setSuccessOpen(false);
    }
  }

  const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setErrorOpen(false);
    }
  }
  
  ;

  return (
    <div>
      <div>
      <Button onClick={handleClickOpen} variant="contained">Edytuj dane</Button>
      </div>
      <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
        <DialogTitle>Wypełnij formularz edycji danych osobowych</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexWrap: 'wrap' }}>
            <form onSubmit={handleSumbit}>
              <List component="nav" aria-label="mailbox folders">
                <ListItem>
                  <div className="form-group" onChange={handleNameChange}>
                    <TextField
                      id="outlined-helperText"
                      label="Imię"
                      defaultValue= {name}
                      helperText="Wprowadź imię o maksymalniej ilości znaków 32"
                    />
                    <div className="form-group">
                      {nameError}
                    </div>
                  </div>
                </ListItem>
                <ListItem>
                  <div className="form-group" onChange={handleSurnameChange}>
                    <TextField
                      id="outlined-helperText"
                      label="Nazwisko"
                      defaultValue= {surname}
                      helperText="Wprowadź nazwisko o maksymalniej ilości znaków 32"
                    />
                    <div className="form-group">
                      {surnameError}
                    </div>
                  </div>
                </ListItem>
              </List>
              <div className="form-group">
                      {dataError}
              </div>
            </form>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleConfirm}>Ok</Button>
        </DialogActions>
      </Dialog>


      <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
        <DialogTitle>Czy na pewno chcesz zmienić dane osobowe użytkownika?</DialogTitle>
        <DialogActions>
          <Button onClick={handleConfirmClose}>Nie</Button>
          <Button onClick={handleConfirmConfirm}>Tak</Button>
        </DialogActions>
      </Dialog>

      <Dialog disableEscapeKeyDown open={successOpen} onClose={handleSuccessClose}>
        <DialogTitle>Dane osobowe zostały zmienione</DialogTitle>
      </Dialog> 

      <Dialog disableEscapeKeyDown open={errorOpen} onClose={handleErrorClose}>
        <DialogTitle>Wystąpił błąd podczas zmiany danych osobowych</DialogTitle>
      </Dialog>
    </div>
  );
}