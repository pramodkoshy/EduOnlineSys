import { ICourseLearningObjects } from 'app/shared/model/course-learning-objects.model';

export interface ICourseUnit {
  id?: number;
  name?: string;
  description?: string;
  courseLearningObjects?: ICourseLearningObjects[];
  courseModuleName?: string;
  courseModuleId?: number;
}

export class CourseUnit implements ICourseUnit {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public courseLearningObjects?: ICourseLearningObjects[],
    public courseModuleName?: string,
    public courseModuleId?: number
  ) {}
}
