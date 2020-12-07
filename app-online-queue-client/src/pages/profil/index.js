import React, {Component} from 'react';
import PropTypes from 'prop-types';
import ProfilForm from "../../components/ProfilForm";
import {Button, Col, Container, ListGroup, ListGroupItem, Row} from "reactstrap";
import {AvField, AvForm} from "availity-reactstrap-validation";
import {connect} from "dva";
import DashboardSidebar from "../../components/DashboardSidebar";
import DashboardLayout from "../../components/DashboardLayout";
import {toast} from "react-toastify";

@connect(({globalModel}) => ({globalModel}))
class Profil extends Component {
  render() {
    const {dispatch, globalModel} = this.props;
    const {currentUser, isAdmin, isModer, isReception, isOperator, isDirector, message} = globalModel;
    const editPassword = (e, v) => {
      if (v.password === v.prePassword) {
          dispatch({
            type: 'globalModel/editPassword',
            payload:
              v
          })
      }else {
        toast.error("Takroriy parollarda xatolik qaytadan kiriting")
      }
    };
    return (

      <div >
        {isAdmin || isModer || isReception || isOperator ? <DashboardLayout pathname={this.props.pathname}>
          <Container>
            <Row className="profil-form">
              <h3 className="h">Parolni tahrirlash</h3>
              <ListGroupItem className={isReception || isOperator || isDirector||isAdmin ? "d-block prof rounded " : "d-none"}>
                <AvForm onValidSubmit={editPassword}>
                  <Col>
                    <AvField className="av" type="text" label="Enter your old password" name="oldPassword" required
                             placeholder="Enter your old password"/>
                    <AvField className="av" type="text" label="Enter your new password" name="password" required
                             placeholder="Enter your new password"/>
                    <AvField className="av" type="text" label="Enter your new pre password" name="prePassword"
                             required
                             placeholder="Enter your new pre password"/>
                    <Button className="but">O'zgartirish</Button>
                  </Col>
                </AvForm>
              </ListGroupItem>
            </Row>
          </Container>
         </DashboardLayout> :
            ""
            }
          </div>
          );
          }
          }
          Profil.propTypes = {};
          export default Profil;
