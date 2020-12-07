import React, {Component} from 'react';
import DashboardLayout from "../../components/DashboardLayout";

class Company extends Component {
  componentDidMount() {
   const {dispatch}=this.props;


  }

  render() {
    const {dispatch}=this.props;

    return (
      <div>

        <DashboardLayout pathname={this.props.pathname}>
          <h1>Company list</h1>

        </DashboardLayout>


      </div>
    );
  }
}

Company.propTypes = {};

export default Company;
