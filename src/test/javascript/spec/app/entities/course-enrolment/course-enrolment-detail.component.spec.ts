import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EduOnlineTestModule } from '../../../test.module';
import { CourseEnrolmentDetailComponent } from 'app/entities/course-enrolment/course-enrolment-detail.component';
import { CourseEnrolment } from 'app/shared/model/course-enrolment.model';

describe('Component Tests', () => {
  describe('CourseEnrolment Management Detail Component', () => {
    let comp: CourseEnrolmentDetailComponent;
    let fixture: ComponentFixture<CourseEnrolmentDetailComponent>;
    const route = ({ data: of({ courseEnrolment: new CourseEnrolment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [CourseEnrolmentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CourseEnrolmentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseEnrolmentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courseEnrolment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courseEnrolment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
