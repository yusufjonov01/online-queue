import React, {Component} from 'react';
import {connect} from "dva";
import DashboardLayout from "../../components/DashboardLayout";
import {Button, Col, Container, Input, InputGroup, InputGroupAddon, Media, Row, Table} from "reactstrap";
import Company from "../company";

@connect(({globalModel, adminModel}) => ({globalModel, adminModel}))
class CompanyInfo extends Component {
  render() {
    const {currentUser, isAdmin, isModer} = this.props.globalModel;

    return (
      <div>

        {isAdmin || isModer ? <DashboardLayout pathname={this.props.pathname}>

            <Container>
              <Row>
                <Col>
                  <div className="addBtn">
                    <h3 className="m-5 ml-0 font-weight-normal"> Salom Siz
                      <b> {currentUser.firstName}</b>
                      siz !</h3>
                    <h1 className="m-5 ml-0 font-weight-bold">Kampaniya haqida malumotlar</h1>

                  </div>
                </Col>
              </Row>
              <Row className="companyInfoStyle">
                <Col className="col-md-6" back>
                  <h6>Nomi :</h6>
                  <h6>INN :</h6>
                  <h6>Direktor FISH :</h6>
                  <h6>Tel raqami :</h6>
                  <h6>Manzili</h6>
                  <h6>Reyting bahosi :</h6>
                  <h6>Operatorlar soni :</h6>
                  <h6>Receptionlar soni :</h6>
                  <h6>Receptionlar soni :</h6>
                  <h6>Bolimlar soni :</h6>
                  <h6>1.XXX</h6>
                  <h6>2.XXX</h6>
                  <h6>3.XXX</h6>
                  <h6>Barcha navbatlar soni :</h6>

                </Col>
                <Col className="col-md-6">
                  <div>
                    <Media>
                      <Media left href="#">
                        <Media object data-src="logo.jpg" alt="Kampaniya fotosi hali yuklanmagan"/>
                        <br/><br/><br/>
                        <p>Azo bolgan vaqti</p>
                        <p>Kim tamonidan tekshirilgan</p>
                        <p>Holati</p>
                        <p>______________</p>
                      </Media>
                      <Media body>

                      </Media>
                    </Media>
                  </div>
                </Col>
              </Row>

            </Container>
          </DashboardLayout> :
          ""
        }

      </div>
    );
  }
}

CompanyInfo.propTypes = {};

export default CompanyInfo;
