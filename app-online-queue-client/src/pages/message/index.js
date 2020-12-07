import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {connect} from "dva";
import {Col, Row, Container, Button, CardFooter, CardHeader, Table, CustomInput, Card, CardBody} from "reactstrap";
import DashboardLayout from "../../components/DashboardLayout";
import Tabs from "react-bootstrap/Tabs";
import Tab from "react-bootstrap/Tab";
import {DiVim} from "react-icons/all";

// import {} from "react-bootstrap/Tab";

@connect(({messageModel}) => ({messageModel}))
class Index extends Component {
  componentDidMount() {
    this.props.dispatch({
      type: 'messageModel/getMessages'
    })
  }


  render() {
    const {dispatch, messageModel} = this.props;
    const {messages, isOpen} = messageModel;


    const isViewChange = (e) => {
      dispatch({
        type: 'messageModel/isViewChange',
        payload: {
          id: e
        },
      });
    };


    return (
      <div>
        <DashboardLayout pathname={this.props.pathname}>
          <Container className="ml-md-3 ml-2">
            <Row>
              <Col className="text-center">
                <h1>Messages</h1>
              </Col>
            </Row>

            <Row>
              {messages.length > 0 ?
                <div>
                  {messages.map((item, i) =>
                    <Row className="my-md-4 my-1" key={i + 1}>
                      <Col className="col-md-8 col-12">
                        <Card>
                          {item.view ?
                            <CardHeader className="bg-success">
                              <h6 className="text-white">O'qilgan</h6>

                              <h6>{item.createdAt}</h6>

                            </CardHeader>
                            :
                            <CardHeader className="bg-danger">
                              <h6 className="text-white">O'qilmagan</h6>

                              <h6 className="text-white">{item.createdAt.substring(0, 10) + ` da yuborilgan`}</h6>

                            </CardHeader>
                          }
                          <CardBody>
                            <h6><i> {item.messageText}</i></h6>
                          </CardBody>
                          {!item.view ?
                            <div>
                              <Button color="success" onClick={() => isViewChange(item.id)}>
                                O'qidim *
                              </Button>
                            </div>
                            :
                            <div>
                              <Button disabled={true}>
                                O'qilgan
                              </Button>
                            </div>
                          }
                        </Card>
                      </Col>
                    </Row>
                  )}
                </div>
                :
                <h1>Messages not Aviable</h1>
              }
            </Row>

          </Container>
        </DashboardLayout>
      </div>
    );
  }
}

Index.propTypes = {};

export default Index;
