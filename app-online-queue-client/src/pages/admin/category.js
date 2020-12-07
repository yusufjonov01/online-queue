import React, {Component} from 'react';
import {connect} from "dva";
import DashboardLayout from "../../components/DashboardLayout";
import {Button, Col, Container, Input, InputGroup, InputGroupAddon, Row, Table} from "reactstrap";

@connect(({globalModel,adminModel}) => ({globalModel,adminModel}))
class Category extends Component {
  render() {
    const {currentUser, isAdmin, isModer} = this.props.globalModel;

    return (
      <div>

        {isAdmin || isModer ? <DashboardLayout pathname={this.props.pathname}>

            <Container>
              <Row>
                <div className="addBtn">
                  <h1 className="center-pill">Admin va moderator categoriy</h1>
                  <h3 className="m-5 ml-0 font-weight-normal"> Salom Siz
                    <b> {currentUser.firstName}</b>
                    siz !</h3>
                  <div>

                    <Row>
                      <Col><Button color="warning">Kategoriya qo'shish</Button>{' '}</Col>

                      <Col className="categorySearch"> <InputGroup>
                        <Input />
                        <InputGroupAddon addonType="append"><Button>Qidirish</Button></InputGroupAddon>
                      </InputGroup>
                      </Col>
                    </Row>
                  </div>

                </div>
              </Row>

              <Row>
                <Table striped>
                  <thead>
                  <tr>
                    <th>T/R</th>
                    <th>O`zbekcha nomi</th>
                    <th>Узбекча криль</th>
                    <th>Русское имя</th>
                    <th>English name</th>
                    <th>Kampaniyalar soni</th>
                    <th>Holati</th>
                    <th>Amallar</th>
                  </tr>
                  </thead>
                  <tbody>
                  {}
                  </tbody>
                </Table>
              </Row>
            </Container>
          </DashboardLayout> :
          ""
        }

      </div>
    );
  }
}

Category.propTypes = {};

export default Category;


// import React, {Component} from 'react';
// import {connect} from "dva";
// import {Button, Container, Row} from "reactstrap";
//
// @connect(({globalModel}) => ({globalModel}))
//
// class Category extends Component {
//   render() {
//
//
//     return (
//
//         <Container>
//           <Row>
//             <div className="addBtn">
//               <h1 className="center-pill">Admin va moderator categoriy</h1>
//               <Button className="color-">Kategoriya qoshish</Button>
//             </div>
//           </Row>
//         </Container>
//
//         );
//         }
//         }
//
//         Category.propTypes = {};
//
//         export default Category;
//
