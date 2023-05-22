import { Moment } from 'moment';
import { ICourseUnit } from 'app/shared/model/course-unit.model';

export interface ICourseModule {
  id?: number;
  name?: string;
  description?: string;
  startDate?: Moment;
  endDate?: Moment;
  courseUnits?: ICourseUnit[];
  courseName?: string;
  courseId?: number;
}

export class CourseModule implements ICourseModule {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public courseUnits?: ICourseUnit[],
    public courseName?: string,
    public courseId?: number
  ) {}
}
