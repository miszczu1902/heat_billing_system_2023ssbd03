import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import NavbarPanel from '../navigation/NavbarPanel';
import { TextField } from '@mui/material';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import axios from 'axios'; 
import validator from "validator";

const GET_DATA_URL = 'http://localhost:8080/api/accounts/self/personal-data';
const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaXN6Y3p1MjEzNyIsImlhdCI6MTY4MzU2MDcyNCwicm9sZSI6Ik9XTkVSIiwiZXhwIjoxNjgzNTYyNTI0fQ.DjIMxOxZ_cliph6fzYVuZZdL3cUDY_scPHloMhy8L5A';

export default function EditPersonalData() {
  const [open, setOpen] = React.useState(false);
  var [name, setName] = React.useState("");
  var [surname, setSurname] = React.useState("");
  var [nameError, setNameError] = React.useState("");
  var [surnameError, setSurnameError] = React.useState("");

  const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
}
  const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (validator.isAlpha(event.target.value) && event.target.value.length <= 32) {
      setNameError("");
      name = event.target.value;
    } else {
      setNameError("Imię może zawierać tylko litery i musi mieć długość do 32 znaków");
    }
  };

  const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => { 
    if (validator.isAlpha(event.target.value) && event.target.value.length <= 32) {
      setSurnameError("");
      surname = event.target.value;
    } else {
      setSurnameError("Nazwisko może zawierać tylko litery i musi mieć długość do 32 znaków");
    } 
  };

  const handleClickOpen = () => {
    axios.get(GET_DATA_URL, {
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

  const handleConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setOpen(false);
    }
    if(nameError === "" && surnameError === "") {
      const data = { firstName: name, surname: surname};
      axios.patch(GET_DATA_URL, {
        headers: {
          Authorization: 'Bearer ' + token
        }, 
        data: data
      })
      .then(response => {
        setName(response.data.firstName.toString());
        setSurname(response.data.surname.toString());
      })
      .catch(error => {
        console.log(error);
      });
    }
  };

  return (
    <div>
      <NavbarPanel/>
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
            </form>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleConfirm}>Ok</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}