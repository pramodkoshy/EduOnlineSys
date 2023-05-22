import { Moment } from 'moment';
import { ICourse } from 'app/shared/model/course.model';

export interface IStudent {
  id?: number;
  name?: string;
  qualifications?: string;
  age?: number;
  grade?: number;
  dateOfBirth?: Moment;
  parentEmail?: string;
  internalUserId?: number;
  courses?: ICourse[];
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public name?: string,
    public qualifications?: string,
    public age?: number,
    public grade?: number,
    public dateOfBirth?: Moment,
    public parentEmail?: string,
    public internalUserId?: number,
    public courses?: ICourse[]
  ) {}
}
