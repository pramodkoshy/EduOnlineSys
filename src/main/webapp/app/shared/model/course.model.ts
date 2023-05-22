import { Moment } from 'moment';
import { ICourseModule } from 'app/shared/model/course-module.model';
import { IStudent } from 'app/shared/model/student.model';

export interface ICourse {
  id?: number;
  name?: string;
  description?: string;
  startDate?: Moment;
  endDate?: Moment;
  courseModules?: ICourseModule[];
  students?: IStudent[];
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public courseModules?: ICourseModule[],
    public students?: IStudent[]
  ) {}
}
