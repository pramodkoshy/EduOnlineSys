import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { EduOnlineTestModule } from '../../../test.module';
import { CourseLearningObjectsComponent } from 'app/entities/course-learning-objects/course-learning-objects.component';
import { CourseLearningObjectsService } from 'app/entities/course-learning-objects/course-learning-objects.service';
import { CourseLearningObjects } from 'app/shared/model/course-learning-objects.model';

describe('Component Tests', () => {
  describe('CourseLearningObjects Management Component', () => {
    let comp: CourseLearningObjectsComponent;
    let fixture: ComponentFixture<CourseLearningObjectsComponent>;
    let service: CourseLearningObjectsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EduOnlineTestModule],
        declarations: [CourseLearningObjectsComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(CourseLearningObjectsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseLearningObjectsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseLearningObjectsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CourseLearningObjects(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.courseLearningObjects && comp.courseLearningObjects[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CourseLearningObjects(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.courseLearningObjects && comp.courseLearningObjects[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
